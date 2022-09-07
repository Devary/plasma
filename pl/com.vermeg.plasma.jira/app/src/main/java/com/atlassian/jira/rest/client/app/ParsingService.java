package com.atlassian.jira.rest.client.app;

import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.app.extraction.StatementExtractorImpl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingService {
    private Issue issue;
    private ArrayList<PlasmaQuery> sqls = new ArrayList<>();
    private boolean regexMatching = false;


    public ParsingService(Issue issue, boolean regexMatching) {
        this.issue = issue;
        this.regexMatching = regexMatching;
        parseIssue();
    }


    public Issue getIssue() {
        return issue;
    }

    public boolean isRegexMatching() {
        return regexMatching;
    }

    private void parseIssue() {
        parseComments(issue.getComments());
    }

    private void parseComments(Iterable<Comment> comments) {
        for (Comment comment : comments) {
            findQueriesFromComment(comment);
        }
    }

    private void findQueriesFromComment(Comment comment) {
        //parse with regex
        if (regexMatching)
            parseCommentUsingRegex(comment);
        else {
            StatementExtractorImpl statementExtractor = new StatementExtractorImpl(issue,comment);
            sqls.addAll(statementExtractor.getSqls());
            //parseCommentUsingstandardMatching(comment);
        }

    }

    private void parseCommentUsingstandardMatching(Comment comment) {
        String body = comment.getBody().toUpperCase();

        String endingWith = ";";
        String codeEnding = "{CODE}";
        String query;
        String startingWith = getStarting(body);
        if (startingWith == null) {
            return;
        }


        String tmpBody = body.substring(body.indexOf(startingWith));
        String finalEnding = tmpBody.contains(endingWith) ? endingWith : codeEnding;
        //init start index
        final int startIndex = body.indexOf(startingWith);
        //init start index
         int endIndex = tmpBody.indexOf(finalEnding)+body.substring(0,startIndex-1).length();

        if (!body.contains(finalEnding)){
            return;
        }

        //Methode ExtractProcedure
        //PROCEDURE MUST BE EXTRACTED WITH FUNCTIONS AND PROCEDURES
        if (startingWith.equals("DECLARE")){
            finalEnding = "END;";
        }

        //TODO : extract from files
        //here


        final int tmpEndIndex= tmpBody.indexOf(finalEnding);
        if ( tmpEndIndex < 0){
            return;
        }
        query = tmpBody.substring(0, tmpEndIndex) ;
        if ( !query.toUpperCase().contains("WHERE")){
            return;
        }
        ///get Final Ending Real Index
        tmpBody.replace(finalEnding," ");

        PlasmaQuery pq = new PlasmaQuery.PlasmaQueryBuilder()
                .withIssue_id(issue.getKey())
                .withModule_id(1)
                .withDescription(null)
                .withProject_id(issue.getProject().getId())
                .withType(0)
                .withOwner(comment.getAuthor().getName())
                .withSql(query+ finalEnding)
                .withStatus(0)
                .withCreated_at(comment.getCreationDate())
                .withIssue(issue)
                .build();
        sqls.add(pq);
        comment.setBody(comment.getBody().replace(comment.getBody().substring(startIndex, endIndex+finalEnding.length()), ""));
        parseCommentUsingstandardMatching(comment);

    }

    private String getStarting(String comment) {
        //order is important
        List<String> list = Arrays.asList("DECLARE","UPDATE","INSERT","SELECT","PROCEDURE", "FUNCTION");

        for (String s : list) {
            if (comment.contains(s)) {
                return s;
            }
        }
        return null;
    }

    private void parseCommentUsingRegex(Comment comment) {
        //String szPattern = "(?is)((?:SELECT[\\s]+[a-z0-9_]+[\\s]*(?:\\(.*?\\))?.*?[\\s]*(?:,(?:(?:[a-z0-9_\\s]+)|(?:[\\s]*.*?\\(.*?\\)[\\s]*.*?)))*)(?:FROM .*?)(?:(?:WHERE(?:(?:[^()]*?)|(?:.*?\\(.*?\\)[\\s]*)))))";
        String szPattern = "^(?=.*SELECT.*FROM)(?!.*(?:CREATE|DROP|UPDATE|INSERT|ALTER|DELETE|ATTACH|DETACH)).*$";
        Pattern p = Pattern.compile(szPattern);
        //Pattern pattern = Pattern.compile(szPattern, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher m = matchString(comment.getBody());
        if (m.find()) {
            PlasmaQuery pq = new PlasmaQuery.PlasmaQueryBuilder()
                    .withIssue_id(issue.getKey())
                    .withModule_id(1)
                    .withDescription(null)
                    .withProject_id(issue.getProject().getId())
                    .withType(0)
                    .withOwner(comment.getAuthor().getName())
                    .withSql(m.group())
                    .withStatus(0)
                    .withCreated_at(comment.getCreationDate())
                    .withIssue(issue)
                    .build();
            sqls.add(pq);
            comment.setBody(comment.getBody().replace(comment.getBody().substring(m.start(), m.end()), ""));
            parseCommentUsingRegex(comment);
        }
    }

    public ArrayList<PlasmaQuery> getSqls() {
        return sqls;
    }

    public Matcher matchString(String str) {
        final String regex = "(?is)((?:SELECT[\\s]+[a-z0-9_]+[\\s]*(?:\\(.*?\\))?.*?[\\s]*(?:,(?:(?:[a-z0-9_\\s]+)|(?:[\\s]*.*?\\(.*?\\)[\\s]*.*?)))*)(?:FROM .*?)(?:(?:WHERE(?:(?:[^()]*?)|(?:.*?\\(.*?\\)[\\s]*)))))";
        //final String string = "Bonjour, Suite à une investigation, on a détécté 3 catégories de polices impactés : * Polices impactés suite à un ajustement :  les movement recalculé ont un flag \"adjustment reference\" qui empêche solife de les détecter mais il traite des lacs qui ont été lié a ce mouvement. Query volumétrique (y compris 4 polices du tableau ) :  {code:sql} SELECT DISTINCT POL.POLICY_NUMBER, PCI.PRODUCT_COMPONENT_NUMBER, POL.POLICY_STATUS_CODEID FROM POLICY POL INNER JOIN PRODUCT_COMPONENT_IMPL PCI ON POL.OID = PCI.POLICY_IMPL_OID INNER JOIN GENERIC_COVERAGE GC ON GC.OWNER_OID = PCI.OID INNER JOIN INVESTMENT_COVERAGE IC ON IC.OID = GC.OID INNER JOIN ACCOUNT ACC ON IC.COVERAGE_ACCOUNT_OID = ACC.OID INNER JOIN POSITION POS ON ACC.OID = POS.ACCOUNT_OID AND POS.POSITION_TYPE_OID = 6000000000000012934 JOIN SUB_POSITION SUBPOS ON SUBPOS.POSITION_OID = POS.OID LEFT OUTER JOIN IDENTIFIER IDE ON IDE.SECURITIES_OID = POS.FININST_OID AND IDE.NATURE = 31 INNER JOIN GUARANTEED_RATE GR ON POS.FININST_OID = GR.OID JOIN ACQUISITION_LINE AL ON AL.POSITION_OID = POS.OID JOIN ACQUISITION_LINE_DECOMPOSITION ALD ON ALD.ACQUISITION_LINE_OID = AL.OID INNER JOIN MOVEMENT MOV ON MOV.OID = ALD.MOVEMENT_OID LEFT OUTER JOIN MOVEMENT_GR ON MOV.OID = MOVEMENT_GR.OID INNER JOIN ACQUISITION_LINE_GR ALGR ON ALGR.OID = AL.OID LEFT JOIN AL_GR_REINV_ANCESTORS ANC ON ANC.ANCESTOR_OID = ALGR.OID WHERE ADJUSTMENT_REFERENCE_OID IS NOT NULL AND MOVEMENT_GR.OID IN ( SELECT ALG.ACQUISITION_LINE_ID_OID FROM ACQUISITION_LINE_GR ALG INNER JOIN ACQUISITION_LINE AL2 ON AL2.OID = ALG.OID WHERE AL2.POSITION_OID = POS.OID AND (AL2.QUANTITY IS NOT NULL AND AL2.QUANTITY > 0) ); {code} +*Procédure de correction :*+  Il faut faire un ajustement et revenir avant le dernier ajustement déjà encodé sur la police.     * Polices impactés suite à un décalage des dates valeur des lacs : Les lacs concerné sont créer en rétro, ce qui engendre un problème d'ordre de traitement au cours du process de recalcule. {code:sql} SELECT DISTINCT POL.POLICY_NUMBER,PCI.PRODUCT_COMPONENT_NUMBER,M.OID FROM POLICY POL INNER JOIN PRODUCT_COMPONENT_IMPL PCI ON POL.OID = PCI.POLICY_IMPL_OID INNER JOIN GENERIC_COVERAGE GC ON GC.OWNER_OID = PCI.OID INNER JOIN INVESTMENT_COVERAGE IC ON IC.OID = GC.OID INNER JOIN ACCOUNT ACC ON IC.COVERAGE_ACCOUNT_OID = ACC.OID INNER JOIN POSITION POS ON ACC.OID = POS.ACCOUNT_OID AND POS.POSITION_TYPE_OID = 6000000000000012934 JOIN SUB_POSITION SUBPOS ON SUBPOS.POSITION_OID = POS.OID LEFT OUTER JOIN IDENTIFIER IDE ON IDE.SECURITIES_OID = POS.FININST_OID AND IDE.NATURE = 31 LEFT OUTER JOIN GUARANTEED_RATE GR ON POS.FININST_OID = GR.OID JOIN ACQUISITION_LINE AL ON AL.POSITION_OID = POS.OID JOIN ACQUISITION_LINE_DECOMPOSITION ALD ON ALD.ACQUISITION_LINE_OID = AL.OID INNER JOIN MOVEMENT M ON M.OID = ALD.MOVEMENT_OID INNER JOIN MOVEMENT_GR MGR ON MGR.OID = M.OID INNER JOIN ACQUISITION_LINE_GR ALG ON ALG.ACQUISITION_LINE_ID_OID = M.OID WHERE EXISTS(SELECT * FROM MOVEMENT_GR MGR2 INNER JOIN MOVEMENT M2 ON M2.OID = MGR2.OID WHERE MGR2.ACQUISITION_LINE_ID_OID = M.OID AND M2.VALUE_DATE < M.VALUE_DATE AND M.AMOUNT = M2.AMOUNT); {code} +*Procédure de correction :*+  Il faut executer la query ci-dessous pour corriger les mouvements concernés, puis relancer le batch de recalcule des AL. {code:sql} DECLARE CURSOR CUR IS SELECT DISTINCT POL.POLICY_NUMBER as polnum, PCI.PRODUCT_COMPONENT_NUMBER, M.OID as mov, m.VALUE_DATE valueDate, m.AMOUNT amount FROM POLICY POL INNER JOIN PRODUCT_COMPONENT_IMPL PCI ON POL.OID = PCI.POLICY_IMPL_OID INNER JOIN GENERIC_COVERAGE GC ON GC.OWNER_OID = PCI.OID INNER JOIN INVESTMENT_COVERAGE IC ON IC.OID = GC.OID INNER JOIN ACCOUNT ACC ON IC.COVERAGE_ACCOUNT_OID = ACC.OID INNER JOIN POSITION POS ON ACC.OID = POS.ACCOUNT_OID AND POS.POSITION_TYPE_OID = 6000000000000012934 JOIN SUB_POSITION SUBPOS ON SUBPOS.POSITION_OID = POS.OID LEFT OUTER JOIN IDENTIFIER IDE ON IDE.SECURITIES_OID = POS.FININST_OID AND IDE.NATURE = 31 LEFT OUTER JOIN GUARANTEED_RATE GR ON POS.FININST_OID = GR.OID JOIN ACQUISITION_LINE AL ON AL.POSITION_OID = POS.OID JOIN ACQUISITION_LINE_DECOMPOSITION ALD ON ALD.ACQUISITION_LINE_OID = AL.OID INNER JOIN MOVEMENT M ON M.OID = ALD.MOVEMENT_OID INNER JOIN MOVEMENT_GR MGR ON MGR.OID = M.OID INNER JOIN ACQUISITION_LINE_GR ALG ON ALG.ACQUISITION_LINE_ID_OID = M.OID WHERE EXISTS(SELECT * FROM MOVEMENT_GR MGR2 INNER JOIN MOVEMENT M2 ON M2.OID = MGR2.OID WHERE MGR2.ACQUISITION_LINE_ID_OID = M.OID AND M2.VALUE_DATE < M.VALUE_DATE AND M.AMOUNT = M2.AMOUNT) and pol.POLICY_STATUS_CODEID <> 5; VAR CUR%ROWTYPE; newValueDate movement.value_date%type; BEGIN DBMS_OUTPUT.PUT_LINE( ' ************************************************************************************************** '); DBMS_OUTPUT.PUT_LINE( ' ******************************** FIX FOR RUNCLV-3328 *********************************************** '); DBMS_OUTPUT.PUT_LINE( ' ************************************************************************************************** '); DBMS_OUTPUT.NEW_LINE(); OPEN CUR; LOOP FETCH CUR INTO VAR; EXIT WHEN CUR%NOTFOUND; DBMS_OUTPUT.PUT_LINE(' POLICY NUMBER : .' || VAR.POLNUM); SELECT m2.VALUE_DATE into newValueDate FROM MOVEMENT_GR MGR2 INNER JOIN MOVEMENT M2 ON M2.OID = MGR2.OID WHERE MGR2.ACQUISITION_LINE_ID_OID = var.mov AND M2.VALUE_DATE < var.valueDate AND var.amount = M2.AMOUNT fetch first 1 rows only; UPDATE MOVEMENT SET VALUE_DATE = newValueDate, MODIFICATION_ACTOR='RUNCLV-3328', MODIFICATION_DATE_TIME= TO_DATE(SYSDATE, 'DD/MM/YYYY') WHERE OID = VAR.mov; DBMS_OUTPUT.PUT_LINE(' MOVEMENT VALUE_DATE UPDATED WITH VALUE.' || newValueDate); END LOOP; DBMS_OUTPUT.NEW_LINE(); DBMS_OUTPUT.PUT_LINE( ' ************************************************************************************************** '); DBMS_OUTPUT.PUT_LINE(CUR%ROWCOUNT || ' ROWS ARE UPDATED.'); DBMS_OUTPUT.PUT_LINE( ' ************************************************************************************************** '); CLOSE CUR; END; {code} * polices qui ont un blocage suite à une opération de withdrawal (y compris la police OFI010003152 );   {code:sql} SELECT distinct pol.POLICY_NUMBER, pci.PRODUCT_COMPONENT_NUMBER,pol.POLICY_STATUS_CODEID FROM POLICY POL INNER JOIN PRODUCT_COMPONENT_IMPL PCI ON POL.OID = PCI.POLICY_IMPL_OID INNER JOIN GENERIC_COVERAGE GC ON GC.OWNER_OID = PCI.OID INNER JOIN INVESTMENT_COVERAGE IC ON IC.OID = GC.OID INNER JOIN ACCOUNT ACC ON IC.COVERAGE_ACCOUNT_OID = ACC.OID INNER JOIN POSITION POS ON ACC.OID = POS.ACCOUNT_OID AND POS.POSITION_TYPE_OID = 6000000000000012934 JOIN SUB_POSITION SUBPOS ON SUBPOS.POSITION_OID = POS.OID LEFT OUTER JOIN IDENTIFIER IDE ON IDE.SECURITIES_OID = POS.FININST_OID AND IDE.NATURE = 31 LEFT OUTER JOIN GUARANTEED_RATE GR ON POS.FININST_OID = GR.OID JOIN ACQUISITION_LINE AL ON AL.POSITION_OID = POS.OID JOIN ACQUISITION_LINE_DECOMPOSITION ALD ON ALD.ACQUISITION_LINE_OID = AL.OID inner join acquisition_line_gr algr on algr.oid = al.oid INNER JOIN MOVEMENT MOV ON MOV.OID = ALD.MOVEMENT_OID INNER JOIN MOVEMENT_GR MGR ON MGR.OID = MOV.OID JOIN ACCOUNTING_TRANSACTION ATR ON ATR.OID = MOV.ACCOUNTING_TRANSACTION_OID JOIN AMOUNT_TYPE SAT ON SAT.CODEID = MOV.SUB_AMOUNT_TYPE_CODEID JOIN client_order clio ON clio.oid = atr.accountable_oid WHERE SAT.EXTERNAL_ID LIKE '%GR_INTEREST%' AND ATR.TRANSACTION_NUMBER LIKE '%WITHDRAWAL/ALLOCATE%' and atr.OPERATION_TYPE <> 'MIGRATION' and clio.ORDER_ORIGIN is null --and pol.POLICY_STATUS_CODEID = 0 and al.QUANTITY <> 0 and ald.QUANTITY <> 0 and mov.CRE_DEB = 1; {code}   *NB :* Pour le reste des cas , sont en cours d'analyse afin de pouvoir les détecter.    Merci. Fakher.";

        final Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str);

    }
}
