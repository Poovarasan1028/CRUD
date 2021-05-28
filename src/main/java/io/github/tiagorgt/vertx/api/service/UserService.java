package io.github.tiagorgt.vertx.api.service;

import io.github.tiagorgt.vertx.api.entity.Info;
import io.github.tiagorgt.vertx.api.entity.User;
import io.github.tiagorgt.vertx.api.repository.UserDao;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by tiago on 07/10/2017.
 */
public class UserService {
    private UserDao userDao = UserDao.getInstance();


    public void list(Handler<AsyncResult<List<User>>> handler) {
        Future<List<User>> future = Future.future();
        future.setHandler(handler);

        User user = null;
        try {
            List<User> result = userDao.findAll();
            System.out.print("List "+result.get(0));
            for(int i=0;i<=result.size();i++) {
            	user.setPhone(result.get(i).getPhone().substring(0, 5)+"XXXXX");
            }
            future.complete(result);
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }
    
    public void listInfo(Handler<AsyncResult<List<Info>>> handler) {
        Future<List<Info>> future = Future.future();
        future.setHandler(handler);

        try {
            List<Info> result = userDao.findAllInfo();
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
            User result = userDao.getByUsername(cpf);
            future.complete(result);
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }
    
    @SuppressWarnings("null")
	public void listUsers(RoutingContext context,Info token, Handler<AsyncResult<List<User>>> handler) {
    	Future<List<User>> future = Future.future();
        future.setHandler(handler);
       
        int seperator = token.getToken().lastIndexOf("-");

        int count=Integer.parseInt(token.getToken().substring(seperator + 1));

        ArrayList<User> user = new ArrayList<User>();
        
        String tokenId=token.getToken().substring(0,seperator);
        
        
        try {
        	if(userDao.getByTokenCheck(tokenId)) {
        		List<User> result = userDao.findAll();
        		
        		if(count==0) {
            		user.addAll(result.subList(0, 10));
            		
            		for(int i=0;i<10;i++) {
            			user.get(i).setName(result.get(i).getName());
            			user.get(i).setEmail(result.get(i).getEmail());
            			user.get(i).setUsername(result.get(i).getUsername());
            			user.get(i).setPassword(result.get(i).getUsername());
            			user.get(i).setPhone(result.get(i).getPhone());
            			user.get(i).setDepartment(result.get(i).getDepartment());
            			
            		}
                    System.out.println("Substring after separator = "+ count);

        		}else {
        			int start=Integer.parseInt(count+"0");
        			int end=Integer.parseInt(count+1+"0"); 
        			
        			System.out.print("Start "+start+"  end "+end);
        			
            		user.addAll(result.subList(start, end));
            		
            		for(int i=0;i<10;i++) {
            			user.get(i).setName(result.subList(start, end).get(i).getName());
            			user.get(i).setEmail(result.subList(start, end).get(i).getEmail());
            			user.get(i).setUsername(result.subList(start, end).get(i).getUsername());
            			user.get(i).setPassword(result.subList(start, end).get(i).getUsername());
            			user.get(i).setPhone(result.subList(start, end).get(i).getPhone());
            			user.get(i).setDepartment(result.subList(start, end).get(i).getDepartment());
            			
            		}
        		}
        		
        		
        		
                future.complete(user);
        	}else {
                sendError("Unauthorized", context.response(),401);
        	}
            
        } catch (Throwable ex) {
        	ex.printStackTrace();
            future.fail(ex);
        }
    }
    
    
    public void logout(Info newUser, Handler<AsyncResult<Info>> handler) {
        Future<Info> future = Future.future();
        future.setHandler(handler);

        Info info=new Info();
        
        try {
        	info=userDao.getByToken(newUser.getToken());
        	userDao.persistInfo(info);            
            future.complete(info);
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }
    
    public void dept(User newUser, Handler<AsyncResult<User>> handler) {
    	System.out.print(newUser.getDepartment());
    	System.out.print(newUser.getUsername());
    	
        Future<User> future = Future.future();
        future.setHandler(handler);

        User user=new User();
        
        try {
        	user=userDao.getForDept(newUser);
        	userDao.persist(user);            
            future.complete(user);
        } catch (Throwable ex) {
            future.fail(ex);
        }
    }

    public void login(RoutingContext context,User newUser, Handler<AsyncResult<Info>> handler) {
        Future<Info> future = Future.future();
        future.setHandler(handler);
       


        try {
        	Info info=new Info();

        	if(newUser.getUsername().isEmpty() && newUser.getPassword().isEmpty()) {
                sendError("Invalid username and password", context.response(),400);
        	}else {
            User user = userDao.getByUsername(newUser.getUsername());
                        
            if(newUser.getUsername().contentEquals((user.getUsername())) && newUser.getPassword().contentEquals(user.getPassword()))
            {
                System.out.print("success");
            	
            	Random r = new java.util.Random ();
            	String s = Long.toString (r.nextLong () & Long.MAX_VALUE, 36);
            	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		Date date = new Date();
            	
            	info.setToken(s);
            	info.setlogin_date(dateFormat.format(date));
            	info.setlogout_date("-");
            	info.setis_active(true);
            	info.setUsername(user.getUsername());
            	
            	userDao.persistInfo(info);
            	
              }else {
                  System.out.print("fail");
              }
        	}
            
            future.complete(info);
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
    
    private void sendError(String errorMessage, HttpServerResponse response,int code) {
        JsonObject jo = new JsonObject();
        jo.put("errorMessage", errorMessage);

        response
                .setStatusCode(code)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(jo));
    }

}
