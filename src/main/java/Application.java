import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;

import static spark.Spark.*;

public class Application {



    public static void main(String[] args) throws IOException {

        port(8080);

        ItemService.makeConnection();
        System.out.println("Insertando un nuevo item");
        Item itemInitial = new Item("MLA","Pelota", "MLA1055",1000,"ARS");
        ItemService.insertItem(itemInitial);


        get("/items", (request, response) -> {
            response.type("application/json");
            ArrayList arrayListItems=ItemService.getAllItems();
            JsonElement gson = new Gson().toJsonTree(arrayListItems);
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, gson));
        });


        get("/item/:item_id", (request, response) -> {
            response.type("application/json");
            String itemId = request.params("item_id");
            if (ItemService.getItemById(itemId)!=null) {
                JsonElement gson = new Gson().toJsonTree(ItemService.getItemById(itemId));
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, gson));
            }else {
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR, "No existe el elemento solicitado"));
            }
        });

        put("/item", (request, response) -> {
            response.type("application/json");
            Item item = new Gson().fromJson(request.body(), Item.class);
            if(ItemService.getItemById(item.getId())!=null) {
                Item itemActualizado = ItemService.updateItemById(item);
                JsonElement gson = new Gson().toJsonTree(itemActualizado);
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS, gson));
            }else{
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR,"No existe el item a modificar..."));
            }
        });


        post("/item", (request, response) -> {
            response.type("application/json");
            Item item  = new Gson().fromJson(request.body(), Item.class);
            Item itemAgregado=ItemService.insertItem(item);
            JsonElement gson=new Gson().toJsonTree(itemAgregado);
            return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS,gson));
        });

        delete("/item/:item_id", (request, response) -> {
            response.type("application/json");
            String itemId = request.params("item_id");
            if(ItemService.getItemById(itemId)!=null) {
                ItemService.deletePersonById(itemId);
                return new Gson().toJson(new StandarResponse(StatusResponse.SUCCESS));
            }
            else {
                return new Gson().toJson(new StandarResponse(StatusResponse.ERROR));
            }
        });
    }
}
