/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actividad_tema2;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author windeveloper
 */
public class GestorEstructuraSGBD {
    
    private static String url="jdbc:mysql://localhost:3306/sql_local001";
    private static String user="root";
    private static String pass="root";
    private static Statement st = null;
    private static PreparedStatement pst = null;
    private static Connection con = null;
    private static String SqlType = null;
    private static ResultSet rs = null;
    

    public GestorEstructuraSGBD(){
        System.out.println("Creando estructura...");
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            System.out.println("La conexión con la base de datos no se pudo realizar");
        }
    }
    
    public void crearEstructura(){
        try{
            st = con.createStatement();
            String tablas[] = new String[5];
            
            
            String tableComercial = "CREATE TABLE Comercial(nif int(2) NOT NULL,zona_id int(2) NOT NULL, nombreComerical varchar(10), "
                    + "CONSTRAINT pk_comercial PRIMARY KEY (nif),"
                    + "CONSTRAINT fk_zonaComercial FOREIGN KEY (zona_id) REFERENCES Zona (idZona) MATCH SIMPLE ON DELETE NO ACTION);";
            
            String tableZona = "CREATE TABLE Zona(idZona int(2) NOT NULL, zona varchar(10),"
                    + "CONSTRAINT pk_zona PRIMARY KEY (idZona));";

            String tableCliente = "CREATE TABLE Cliente(dni int(2) NOT NULL AUTO_INCREMENT, sector_id int(2) NOT NULL,zona_id int(2) NOT NULL, nombreCliente varchar(10),"
                    + "CONSTRAINT pk_cliente PRIMARY KEY (dni),"
                    + "CONSTRAINT fk_sector FOREIGN KEY (sector_id) REFERENCES Sector (idSector) MATCH SIMPLE ON DELETE NO ACTION,"
                    + "CONSTRAINT fk_zona FOREIGN KEY (zona_id) REFERENCES Zona (idZona) MATCH SIMPLE ON DELETE NO ACTION);";
          
            
            String tableSector = "CREATE TABLE Sector(idSector int(2) NOT NULL, sector varchar(10),"
                    + "CONSTRAINT pk_sector PRIMARY KEY (idSector));";
           
            
            String tableArticulo = "CREATE TABLE Articulo(idArticulo int(2) NOT NULL AUTO_INCREMENT, sector_id int(2) NOT  NULL, nombreArticulo varchar(10),"
                    + "CONSTRAINT pk_articulo PRIMARY KEY (idArticulo),"
                    + "CONSTRAINT fk_sectorArticulo FOREIGN KEY (sector_id) REFERENCES Sector (idSector) MATCH SIMPLE ON DELETE NO ACTION);";
 
            tablas[0] = tableZona;
            tablas[1] = tableSector;
            tablas[2] = tableCliente;
            tablas[3] = tableComercial;
            tablas[4] = tableArticulo;
            
            for(int i = 0; i<tablas.length;i++){
                st.executeUpdate(tablas[i]);
                System.out.println("Tabla creada");
            }
            
            System.out.println("Estructura creada");
        }
        catch(SQLException e){
            System.out.println("Fallo al crear las tablas \n" + e.getMessage());
        }
    }
    
    public void eliminarEstructura(){
        try{
            
            //Para eliminar las tablas sin problemas debemos eliminar las constraints con DELETE CONSTRAINT.
            st=con.createStatement();
            String tablas[] = new String[5];
            
            String tableComercial = "DROP TABLE IF EXISTS Comercial;";
            
            String tableZona = "DROP TABLE IF EXISTS Zona;";
            
            String tableCliente = "DROP TABLE IF EXISTS Cliente;";
            
            String tableSector = "DROP TABLE IF EXISTS Sector;";
            
            String tableArticulo = "DROP TABLE IF EXISTS Articulo;";
            
            tablas[0] = tableArticulo;
            tablas[1] = tableComercial;
            tablas[2] = tableCliente;
            tablas[3] = tableSector;
            tablas[4] = tableZona;
            
            for(int i = 0; i<tablas.length;i++){
                st.executeUpdate(tablas[i]);
                System.out.println("Tabla eliminada");
            }
            
            System.out.println("Estructura eliminada");
        }
        catch(SQLException e){
            System.out.println("Fallo al eliminar las tablas \n" + e.getMessage());
        }
    }
    
    public void modificacionTablas(String SqlType){
        try{
            
            
            //Recomendable utilizar primero el comando insert, después el update y después el delete
            String modificar = "update";
            String eliminar = "delete";
            String insertar ="insert";
            st = con.createStatement();
        
            if (SqlType.equalsIgnoreCase(modificar)){
                modificar = "UPDATE Sector SET sector = 'Tecnología' WHERE idSector = 1";
                st.executeUpdate(modificar);
                System.out.println("La opción " + SqlType + " fue correctamente ejecutada");
            
            }
        
            else if(SqlType.equalsIgnoreCase(eliminar)){
                eliminar = "DELETE FROM Articulo WHERE idSector = '1'";
                st.executeUpdate(eliminar);
                System.out.println("La opción " + SqlType + " fue correctamente ejecutada");
              
            }
            
            else if(SqlType.equalsIgnoreCase(insertar)){
                insertar = "INSERT INTO Sector VALUES (1, 'Tecnologia');";
                        
                st.executeUpdate(insertar);
                System.out.println("La opción " + SqlType + " fue correctamente ejecutada");
            }
            
            else{
                System.out.println("La opción " + SqlType+ " no és una opción válida");
            }
            
            
        }
        catch(SQLException e){
            System.out.println("La modificación no se pudo realizar \n" + e.getMessage());
        }
        
        
    }
    
    public void recuperarDatos(){
        try{
            String seleccionar = "SELECT idArticulo,nombreArticulo FROM ARTICULO";
            st = con.createStatement();
            
            rs = st.executeQuery(seleccionar);
            System.out.println("Articulos: ");
            while(rs.next()){
                System.out.println(rs.getString("idArticulo")+" - "+ rs.getString("nombreArticulo"));
            }

            
            
            
            
        }
        catch(SQLException e){
            System.out.println("No ha sido posible recuperar los datos \n" + e.getMessage());
        }
        
        
    }
    
    public ArrayList<Articulo> obtenerArticulosPorDescripcion(String descripcion){
        try{
        
            ArrayList<Articulo> lista = new ArrayList();
            String query = "SELECT idArticulo,nombreArticulo FROM ARTICULO WHERE nombreArticulo LIKE ?;";
            pst = con.prepareStatement(query);
            pst.setString(1, descripcion+"%");
        
        
            rs = pst.executeQuery();
            while(rs.next()){
                lista.add(new Articulo(rs.getString("nombreArticulo")));
                System.out.println("Articulo agregado con éxito");
            }
        
            return lista;
        }
        
        catch(SQLException e){
            System.out.println("No se pudo obtener el artículo \n" + e.getMessage());
            return null;
        }
        
        
    }
    
    public ArrayList<Articulo> obtenerArticuloPorId(int id){
         try{
        
            ArrayList<Articulo> lista = new ArrayList();
            String query = "SELECT idArticulo,nombreArticulo FROM ARTICULO WHERE idArticulo = ?;";
            pst = con.prepareStatement(query);
            pst.setInt(1, id);
        
        
            rs = pst.executeQuery();
            while(rs.next()){
                lista.add(new Articulo(rs.getString("idArticulo"))) //También podemos utilizar el comando rs.getInt(1);
                System.out.println("Articulo agregado");
            }
        
            return lista;
        }
        
        catch(SQLException e){
            System.out.println("No se pudo obtener el artículo \n" + e.getMessage());
            return null;
        }
    }
   
    
    public static void main(String[] args){
        Scanner entrada = new Scanner(System.in);
        
        GestorEstructuraSGBD ge = new GestorEstructuraSGBD();
        boolean creado = false;
        
        //Crear y eliminar la estructura
        ge.eliminarEstructura();
        ge.crearEstructura();
        creado = true;
        
        
      
        //Modificar tablas
        System.out.println("Introduce la opción que quieras realizar (insert,update o delete)");
            
        String SqlType = entrada.next();
        ge.modificacionTablas(SqlType);
            
        //Recuperar datos
        if(creado==true){
            ge.recuperarDatos();
            
            //Obtener articulos por descripcion
            ge.obtenerArticulosPorDescripcion("Tablet");
            
            //Obtener articulos por id
            ge.obtenerArticuloPorId(1);
        }
        else{
            System.out.println("Las tablas no fueron creadas");
        }
          
            
        //Cerrar conexión
        try{
            con.close();
        }
        catch(SQLException e){
            System.out.println("No se pudo cerrar la conexión");
        }
       
        
       
    }
    
}
