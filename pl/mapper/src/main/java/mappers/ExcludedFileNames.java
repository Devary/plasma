package mappers;

abstract class ExcludedFileNames {
    /* Ends With */
    public static final String FORM_FACADE_MAPPER = "FormFacadeMapper";
    public static final String QUERY = "Query";
    public static final String BIZ_TASK_DELEGATE = "BizTaskDelegate";
    public static final String DAO_IMPL = "DaoImpl";
    public static final String SERVICE_IMPL = "ServiceImpl";
    public static final String EXCEPTION = "Exception";
    public static final String HANDLER = "Handler";
    public static final String ROW_FACADE_MAPPER = "RowFacadeMapper";
    public static final String ROW_FORM_FACADE_MAPPER = "RowFormFacadeMapper";
    public static final String CONSTANTS = "Constants";
    public static final String CLONING_FILTER = "CloningFilter";
    public static final String VIEW = "View";
    public static final String HELPER = "Helper";
    public static final String TASK = "Task";
    public static final String SERVICE_FACTORY = "ServiceFactory";
    public static final String SELECTOR = "Selector";
    public static final String VIEW_STATE = "ViewState";
    public static final String FORM_FACADE = "FormFacade";
    public static final String OQL_FINDER = "OQLFinder";
    public static final String PERMISSION = "Permission";
    public static final String STATE_MACHINE = "StateMachine";
    public static final String EXTRACTOR = "Extractor";
    public static final String EXTRACTOR_ADAPTER = "ExtractorAdapter";
    public static final String PROVIDER = "Provider";

    /* contains */
    public static final String BY = "By";
    public static final String WIZ = "Wiz";

    public static final String[] byQuery = {BY,QUERY};


    public static final String[] enders = {FORM_FACADE_MAPPER ,QUERY ,BIZ_TASK_DELEGATE ,DAO_IMPL ,SERVICE_IMPL ,EXCEPTION ,
            HANDLER ,ROW_FACADE_MAPPER ,ROW_FORM_FACADE_MAPPER ,CONSTANTS ,CLONING_FILTER ,VIEW ,HELPER ,TASK ,
            SERVICE_FACTORY ,SELECTOR ,VIEW_STATE ,FORM_FACADE ,OQL_FINDER ,PERMISSION ,STATE_MACHINE,EXTRACTOR,
            EXTRACTOR_ADAPTER,PROVIDER};


    public static final String[] containers = {BY,WIZ};

}
