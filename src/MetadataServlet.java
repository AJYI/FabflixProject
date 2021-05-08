import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(name = "metadataServ", urlPatterns = "/fabflix/MetadataInfo/metadata")
public class MetadataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData databaseMetaData = conn.getMetaData();

            /*
            SOURCE: Helped a lot for reading meta data :https://www.codejava.net/java-se/jdbc/how-to-read-database-meta-data-in-jdbc
            SOURCE: Helped a lot for reading meta data :https://www.progress.com/blogs/jdbc-tutorial-extracting-database-metadata-via-jdbc-driver
            SOURCE: https://stackoverflow.com/questions/11197818/how-do-i-make-a-json-object-with-multiple-arrays
             */

            ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});

            // Json Array
            JsonArray jsonArray = new JsonArray();

            while(resultSet.next())
            {
                // To get rid of sys_config from our results
                if(resultSet.getString(3).equals("sys_config")){
                    continue;
                }
                // This should be JSON
                String tables = resultSet.getString(3);

                ResultSet resultSetColumns = databaseMetaData.getColumns(null, null, resultSet.getString(3), null);
                ResultSet resultSetPrimaryKey = databaseMetaData.getPrimaryKeys(null, null, resultSet.getString(3));

                //JsonObject semiObject = new JsonObject();
                ArrayList<String> rows = new ArrayList<String>();
                while(resultSetColumns.next()){
                    String metaRow = "\t" + resultSetColumns.getString("COLUMN_NAME") + " - " + resultSetColumns.getString("TYPE_NAME") + "(" + resultSetColumns.getInt("COLUMN_SIZE") + ")";
                    rows.add(metaRow);
                }
                // Jsonification here
                JsonObject jsonObject = new JsonObject();
                for(int i = 0; i < rows.size(); i++){
                    jsonObject.addProperty(""+ (i + 1), rows.get(i));
                }
                jsonObject.addProperty("0", tables);
                jsonArray.add(jsonObject);
            }

            System.out.println(jsonArray.toString());

            resultSet.close();

            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {

            // write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }
    }
}
