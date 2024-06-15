package gerenciaconfigsrc.configs.errorHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptionMethod(Exception ex, WebRequest request) {

        ExceptionMessage exceptionMessageObj = new ExceptionMessage();

        if(ex instanceof MethodArgumentNotValidException) {
            StringBuilder sb = new StringBuilder();
            final FieldError fieldError = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldError();//((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();

            sb.append(fieldError.getDefaultMessage());
            sb.append(";");

            exceptionMessageObj.setMessage(sb.toString());
        }else{
            exceptionMessageObj.setMessage(ex.getLocalizedMessage());//"Requisição inválida, verifique dados!");
        }

        exceptionMessageObj.setError(ex.getClass().getCanonicalName());
        exceptionMessageObj.setPath(((ServletWebRequest) request).getRequest().getServletPath());

        return new ResponseEntity<>(exceptionMessageObj, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}