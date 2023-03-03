package backend.team.backend.transversal.response.rest;


import backend.team.backend.transversal.utilitaries.UtilObject;
import backend.team.backend.transversal.utilitaries.UtilText;

import java.util.ArrayList;
import java.util.List;

public class Response<T>{

    private List<String> responseMessages;

    public void agregarMensaje(String message) {
        if(!UtilText.isEmpty(message)){
            getResponseMessages().add(message);
        }
    }

    public List<String> getResponseMessages() {
        if(UtilObject.isNull(responseMessages)){
            setResponseMessages(responseMessages);
        }
        return responseMessages;
    }

    public void setResponseMessages(List<String> responseMessages) {
        this.responseMessages = UtilObject.defaultValue(responseMessages, new ArrayList<>());
    }
}
