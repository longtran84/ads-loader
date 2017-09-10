package controllers;

import play.Logger;

import play.data.Form;
import play.mvc.*;
import java.util.List;
import io.swagger.annotations.*;
import static play.libs.Json.toJson;

@Api
public class TodoController extends Controller {
    public Result getAllTodos() {
        return ok("Test");
    }
    
	public Result redirectDocs() {
		return ok("redirect:/swagger.json");
	}
}