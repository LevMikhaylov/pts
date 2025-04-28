@ControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> handleRuntimeException(RuntimeException e) {
        Response response = new Response("Произошла ошибка во время выполнения: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
    }

}