package io.github.tiagorgt.vertx.api;

import io.github.tiagorgt.vertx.api.entity.Info;
import io.github.tiagorgt.vertx.api.entity.User;
import io.github.tiagorgt.vertx.api.service.PositionService;
import io.github.tiagorgt.vertx.api.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.Set;

public class Verticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        Router router = Router.router(vertx); // <1>
        // CORS support
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PUT);

        router.route().handler(CorsHandler.create("*") // <2>
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));
        router.route().handler(BodyHandler.create()); // <3>

        // routes
        router.get("/position").handler(this::getPositions);
        router.get("/user/:id").handler(this::getById);
        router.get("/info").handler(this::getInfo);
        router.get("/department").handler(this::getDepartment);

        
        router.post("/user").handler(this::getUsers);
        router.post("/login").handler(this::login);
        router.post("/logout").handler(this::logout);
        router.post("/usersave").handler(this::save);
        router.post("/department").handler(this::addDept);

        
        router.put("/user").handler(this::update);
        
        router.delete("/user/:id").handler(this::remove);
        router.post("/user/filter").handler(this::getUsersByFilter);

        vertx.createHttpServer() // <4>
                .requestHandler(router::accept)
                .listen(8080, "0.0.0.0", result -> {
                    if (result.succeeded())
                        fut.complete();
                    else
                        fut.fail(result.cause());
                });
    }


    PositionService positionService = new PositionService();
    UserService userService = new UserService();

    private void getPositions(RoutingContext context) {
        positionService.list(ar -> {
            if (ar.succeeded()) {
                sendSuccess(Json.encodePrettily(ar.result()), context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }
    
    private void getDepartment(RoutingContext context) {
        positionService.listDepartment(ar -> {
            if (ar.succeeded()) {
                sendSuccess(Json.encodePrettily(ar.result()), context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }
    
    private void getInfo(RoutingContext context) {
        userService.listInfo(ar -> {
            if (ar.succeeded()) {
                sendSuccess(Json.encodePrettily(ar.result()), context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }

    private void getUsers(RoutingContext context) {
        userService.listUsers(context,Json.decodeValue(context.getBodyAsString(), Info.class), ar -> {
            if (ar.succeeded()) {
                sendSuccess(Json.encodePrettily(ar.result()), context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }
    
//    private void getUsers(RoutingContext context) {
//        userService.list(ar -> {
//            if (ar.succeeded()) {
//                sendSuccess(Json.encodePrettily(ar.result()), context.response());
//            } else {
//                sendError(ar.cause().getMessage(), context.response());
//            }
//        });
//    }

    private void getUsersByFilter(RoutingContext context){
        userService.getByFilter(context.getBodyAsJson(), ar -> {
            if (ar.succeeded()) {
                sendSuccess(Json.encodePrettily(ar.result()), context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }

    private void getById(RoutingContext context) {
        userService.getById(context.request().getParam("id"), ar -> {
            if (ar.succeeded()) {
                if (ar.result() != null){
                    sendSuccess(Json.encodePrettily(ar.result()), context.response());
                } else {
                    sendSuccess(context.response());
                }
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }
    
    private void login(RoutingContext context) {
        userService.login(context,Json.decodeValue(context.getBodyAsString(), User.class), ar -> {
            if (ar.succeeded()) {
                sendSuccess(Json.encodePrettily(ar.result()), context.response());
            } else {
                sendError("Invalid username and password", context.response());
            }
        });
    }
    
    private void logout(RoutingContext context) {
        userService.logout(Json.decodeValue(context.getBodyAsString(), Info.class), ar -> {
            if (ar.succeeded()) {
                sendSuccess(Json.encodePrettily(ar.result()), context.response());
            } else {
                sendError("Token not available.", context.response());
            }
        });
    }
    
    private void addDept(RoutingContext context) {
        userService.dept(Json.decodeValue(context.getBodyAsString(), User.class), ar -> {
            if (ar.succeeded()) {
                sendSuccess(Json.encodePrettily(ar.result()), context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }

    private void save(RoutingContext context) {
        userService.save(Json.decodeValue(context.getBodyAsString(), User.class), ar -> {
            if (ar.succeeded()) {
                sendSuccess(context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }
    
    
    private void update(RoutingContext context) {
        userService.update(Json.decodeValue(context.getBodyAsString(), User.class), ar -> {
            if (ar.succeeded()) {
                sendSuccess(context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }

    private void remove(RoutingContext context) {
        userService.remove(context.request().getParam("id"), ar -> {
            if (ar.succeeded()) {
                sendSuccess(context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }

    private void sendError(String errorMessage, HttpServerResponse response) {
        JsonObject jo = new JsonObject();
        jo.put("errorMessage", errorMessage);

        response
                .setStatusCode(400)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(jo));
    }

    private void sendSuccess(HttpServerResponse response) {
        response
                .setStatusCode(200)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end();
    }

    private void sendSuccess(String responseBody, HttpServerResponse response) {
        response
                .setStatusCode(200)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(responseBody);
    }
}
