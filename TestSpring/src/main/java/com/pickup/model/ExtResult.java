package com.pickup.model;

public class ExtResult {

    //private ExtMetaData metaData;
    private String success = "true";
    private String msg = "";
    private Object data;
    private Exception exception;
  ///  private List<FieldError> errors;
    private Integer results;

    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
	public Integer getResults() {
        return results;
    }

    public void setResults(Integer results) {
		this.results = results;
	}

   // public ExtMetaData getMetaData() {
   //     return metaData;
   // }

    //public void setMetaData(ExtMetaData metaData) {
    //    this.metaData = metaData;
    //}

    public Object getData() {
        return data;
    }

    public ExtResult setData(Object data) {
        this.data = data;
        return this;
    }

   // protected void checkErrors() {
    //    if (errors == null) {
      //  	errors = new ArrayList<FieldError>();
       // }
    //}

   // public List<FieldError> getErrors() {
    //    return errors;
    //}

   // public void addError(String key, String message) {
    //    checkErrors();
     //   errors.add( new FieldError(key, message));
   // }

  //  protected void setErrors(List<FieldError> errors) {
  //      this.errors = errors;
   // }

    public String getExceptionMessage() {
        return exception != null ? exception.getMessage() : null;
    }

    //public String getExceptionStackTrace() {
     //   if(exception != null) {
          //  StringWriter sw = new StringWriter();
           // exception.printStackTrace(new PrintWriter(sw));
           // return sw.toString();
       // } else {
        //    return null;
       // }
   // }

    public void setException(Exception exception) {
        this.exception = exception;
    }
    
    @Override
    public String toString(){
    	return "success="+this.success+" msg="+this.msg;
    }

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
    
    
    
    
}