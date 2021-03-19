
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;


public class App {

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    public static void main(String[] args) {

        String dbDiskURL = "jdbc:h2:file:./greetdb";
//        String dbMemoryURL = "jdbc:h2:mem:greetdb";

        Jdbi jdbi = Jdbi.create(dbDiskURL, "sa", "");

// get a handle to the database
        Handle handle = jdbi.open();

// create the table if needed
        handle.execute("create table if not exists student ( id integer identity, name varchar(50), age int,grade int )");


        port(getHerokuAssignedPort());

// STUDENT SECTION

        get("/tutor", (req, res) -> {

            Map<String, Object> map = new HashMap<>();


            return new ModelAndView(map, "tutor.handlebars");

        }, new HandlebarsTemplateEngine());

        get("/intro", (req, res) -> {

            Map<String, Object> map = new HashMap<>();


            return new ModelAndView(map, "introscreen.handlebars");

        }, new HandlebarsTemplateEngine());


        get("/student", (req, res) -> {

            Map<String, Object> map = new HashMap<>();

            List<String> nameList=handle.createQuery("select name from student")
                    .mapTo(String.class)
                    .list();
            map.put("names",nameList);
            return new ModelAndView(map, "student.handlebars");

        }, new HandlebarsTemplateEngine());

        post("/student", (req, res) -> {

            Map<String, Object> map = new HashMap<>();

            handle.execute("insert into student (name,age,grade) values ('Sesethu Dumezweni',12,5)");
            handle.execute("insert into student (name,age,grade) values ('Phumlani',12,5)");
            handle.execute("insert into student (name,age,grade) values ('Lubha Dumezweni',12,5)");
            return new ModelAndView(map, "student.handlebars");

        }, new HandlebarsTemplateEngine());



    }
}
