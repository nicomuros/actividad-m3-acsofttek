package com.softtek.m3.repositorio;

import com.softtek.m3.db.DBConnection;
import com.softtek.m3.db.MySQLConnection;
import com.softtek.m3.excepciones.CRUDException;
import com.softtek.m3.modelo.Tarea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TareaRepositorioImpl implements TareaRepositorio{

    // Se declara la dependencia que será inyectada
    DBConnection databaseConnection;

    public TareaRepositorioImpl(DBConnection databaseConnection){

        // Se instancia la dependencia inyectada
        this.databaseConnection = databaseConnection;
    }

    @Override
    public void agregarTarea(Tarea tarea) {

        // Consulta SQL
        String query = "INSERT INTO todolist(titulo, descripcion) VALUES(?, ?)";

        // Try-with-resources para iniciar y cerrar las conexiones a db automaticamente
        try (Connection conn = databaseConnection.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(query);){

            // Parametrización de la consulta SQL
            ps.setString(1, tarea.getTitulo());
            ps.setString(2, tarea.getDescripcion());

            // Se ejecuta la consulta a la base de datos
            ps.execute();
        } catch (SQLException e) {
            throw new CRUDException("Error al agregar una tarea: " + e.getMessage());
        }
    }

    @Override
    public List<Tarea> seleccionarTodasLasTareas(){

        // Tipo de objeto que se va a recibir
        List<Tarea> listaDeTareas = new ArrayList<>();

        // Consulta SQL
        String query = "SELECT * FROM todolist";

        // Try-with-resources para iniciar y cerrar las conexiones a db automaticamente
        try (Connection conn = databaseConnection.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(query);  ){

            // Se ejecuta la consulta a la base de datos
            ResultSet rs = ps.executeQuery();

            // Se recorre la respuesta sql, se crea el objeto tarea, y se carga a la lista
            while (rs.next()){
                Tarea tarea = new Tarea();
                tarea.setId(rs.getInt("id"));
                tarea.setTitulo(rs.getString("titulo"));
                tarea.setDescripcion(rs.getString("descripcion"));
                listaDeTareas.add(tarea);
            }

        } catch (SQLException e){
            throw new CRUDException("Error al solicitar todas las tareas: " + e.getMessage());
        }

        // Se retorna la lista
        return listaDeTareas;
    }

    @Override
    public Boolean existeTareaPorId(Integer tareaId) {

        // Tipo de objeto que se retornara
        boolean existe = false;

        // Consulta SQL
        String query = "SELECT count(id) FROM todolist WHERE id=?";

        // Try-with-resources para iniciar y cerrar las conexiones a db automaticamente
        try (Connection conn = databaseConnection.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(query);){

            // Parametrización de la consulta SQL
            ps.setInt(1, tareaId);

            // Se ejecuta la consulta y se almacena la respuesta
            ResultSet rs = ps.executeQuery();

            // Compruebo si la cantidad de ids que cumplen la condición es mayor a 0 (solo puede ser 1)
            if (rs.next()){
                existe = rs.getInt(1) > 0;
            }
        } catch (SQLException e){
            throw new CRUDException("Error al verificar si existe la tarea con el id "
                    + tareaId + ": " + e.getMessage());
        }

        // Se retorna el resultado
        return existe;
    }

    @Override
    public Optional<Tarea> seleccionarTareaPorId(Integer tareaId) {

        // Declaración e instanciación de resultado. Tipo opcional porque puede o no tener valores (null / dato)
        Optional<Tarea> result = Optional.empty();

        // Consulta SQL
        String query = "SELECT id, titulo, descripcion FROM todolist WHERE id=?";

        // Try-with-resources para iniciar y cerrar las conexiones a db automaticamente
        try (Connection conn = databaseConnection.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(query);){

            // Parametrización de consulta SQL
            ps.setInt(1, tareaId);

            // Se ejecuta la consulta
            ResultSet rs = ps.executeQuery();

            // Se recorre y carga la respuesta en el resultado
            if (rs.next()){
                Tarea tarea = new Tarea();
                tarea.setId(rs.getInt("id"));
                tarea.setTitulo(rs.getString("titulo"));
                tarea.setDescripcion(rs.getString("descripcion"));
                result = Optional.of(tarea);
            }
        } catch (SQLException e){
            throw new CRUDException("Error al obtener la tarea con id " + tareaId + ": " + e.getMessage());
        }

        // Se retorna el resultado, que puede contener una Tarea o ser null
        return result;
    }

    @Override
    public void modificarTarea(Tarea tarea) {

        // Consulta SQL
        String query = "UPDATE todolist SET titulo = ?, descripcion = ? WHERE id = ?";

        // Try-with-resources para iniciar y cerrar las conexiones a db automaticamente
        try ( Connection conn = databaseConnection.establecerConexion();
                PreparedStatement ps = conn.prepareStatement(query); ){

            // Parametrización de la consulta SQL
            ps.setString(1, tarea.getTitulo());
            ps.setString(2,tarea.getDescripcion());
            ps.setInt(3, tarea.getId());

            // Se ejecuta la consulta
            ps.execute();
        } catch (SQLException e) {
            throw new CRUDException("Error al actualizar el producto con id:" + tarea.getId() + ": " + e.getMessage());
        }
    }

    @Override
    public void eliminarTareaPorId(Integer tareaId) {

        // Consulta SQL
        String query = "DELETE FROM todolist WHERE ID = ?";

        // Try-with-resources para iniciar y cerrar las conexiones a db automaticamente
        try ( Connection conn = databaseConnection.establecerConexion();
                PreparedStatement ps = conn.prepareStatement(query);){

            // Parametrización de la consulta SQL
            ps.setInt(1, tareaId);

            // Se ejecuta la consulta
            ps.execute();
        } catch (SQLException e){
            throw new CRUDException("Error al eliminar tarea con id: " + tareaId + ": " + e.getMessage());
        }
    }
}
