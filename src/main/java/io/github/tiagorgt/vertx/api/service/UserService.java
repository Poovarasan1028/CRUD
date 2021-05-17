package io.github.tiagorgt.vertx.api.service;

import io.github.tiagorgt.vertx.api.entity.User;
import io.github.tiagorgt.vertx.api.repository.UserDao;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * Created by tiago on 07/10/2017.
 */
public class UserService {
    private UserDao userDao = UserDao.getInstance();

    public void list(Handler<AsyncResult<List<User>>> handler) {
        Future<List<User>> future = Future.future();
        future.setHandler(handler);

        try {
            List<User> result = userDao.findAll();
            future.complete(result);
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }

    public void getByFilter(JsonObject filter, Handler<AsyncResult<List<User>>> handler){
        Future<List<User>> future = Future.future();
        future.setHandler(handler);

        try {
            List<User> result = userDao.getByFilter(filter);
            future.complete(result);
        } catch (Exception ex){
            future.fail(ex);
        }
    }

    public void getById(String cpf, Handler<AsyncResult<User>> handler) {
        Future<User> future = Future.future();
        future.setHandler(handler);

        try {
            User result = userDao.getById(cpf);
            future.complete(result);
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }
    
    public void login(RoutingContext context,User newUser, Handler<AsyncResult<JsonObject>> handler) {
        Future<JsonObject> future = Future.future();
        future.setHandler(handler);
        JsonObject jo = new JsonObject();
       

        try {
        	if(newUser.getUsername().isEmpty() && newUser.getPassword().isEmpty()) {
                sendError("Invalid username and password", context.response());
        	}else {
            User user = userDao.getByUsername(newUser.getUsername());
            
            if(newUser.getUsername().contentEquals((user.getUsername())) && newUser.getPassword().contentEquals(user.getPassword()))
            {
            	jo.put("status",true);
            }else {
            	jo.put("status",false);
            }
        	}
            
            future.complete(jo);
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }


    public void save(User newUser, Handler<AsyncResult<User>> handler) {
        Future<User> future = Future.future();
        future.setHandler(handler);

        try {
            User user = userDao.getById(newUser.getName());

            if (user != null) {
                future.fail("Usuário já incluído.");
                return;
            }
            userDao.persist(newUser);
            future.complete();
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }

    public void update(User user, Handler<AsyncResult<User>> handler) {
        Future<User> future = Future.future();
        future.setHandler(handler);

        try {
            userDao.merge(user);
            future.complete();
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }

    public void remove(String cpf, Handler<AsyncResult<User>> handler) {
        Future<User> future = Future.future();
        future.setHandler(handler);

        try {
            userDao.removeById(cpf);
            future.complete();
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }
    
    private void sendError(String errorMessage, HttpServerResponse response) {
        JsonObject jo = new JsonObject();
        jo.put("errorMessage", errorMessage);

        response
                .setStatusCode(400)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(jo));
    }

}
