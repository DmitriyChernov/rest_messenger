package my.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceExists extends Exception{

    private static final long serialVersionUID = 1L;

    public ResourceExists(String message){
        super(message);
    }
}