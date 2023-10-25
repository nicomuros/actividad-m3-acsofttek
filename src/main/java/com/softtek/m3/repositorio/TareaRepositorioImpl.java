package com.softtek.m3.repositorio;

import com.softtek.m3.db.DBConnection;
import com.softtek.m3.db.MySQLConnection;
import com.softtek.m3.exepciones.CRUDException;
import com.softtek.m3.modelo.Tarea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TareaRepositorioImpl implements TareaRepositorio{

    DBConnection connector = new MySQLConnection();

    @Override
    public void agregarTarea(Tarea tarea) {
        String query = "INSERT INTO todolist(titulo, descripcion) VALUES(?, ?)";

        try (Connection conn = connector.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(query);
        ){

            ps.setString(1, tarea.getTitulo());
            ps.setString(2, tarea.getDescripcion());
            ps.execute();
        } catch (SQLException e) {
            throw new CRUDException("Error al agregar una tarea: " + e.getMessage());
        }
    }

    @Override
    public List<Tarea> seleccionarTodasLasTareas(){
        List<Tarea> listaDeTareas = new ArrayList<>();
        String query = "SELECT * FROM todolist";
        try (Connection conn = connector.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ){

            ResultSet rs = ps.executeQuery();
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
        return listaDeTareas;
    }

    @Override
    public Boolean existeTareaPorId(Integer tareaId) {
        boolean existe = false;
        String query = "SELECT count(id) FROM todolist WHERE id=?";

        try (Connection conn = connector.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ){

            ps.setInt(1, tareaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                existe = rs.getInt(1) > 0;
            }
        } catch (SQLException e){
            throw new CRUDException("Error al verificar si existe la tarea con el id " + tareaId + ": " + e.getMessage());
        }
        return existe;
    }

    @Override
    public Optional<Tarea> seleccionarTareaPorId(Integer tareaId) {
        Optional<Tarea> result = Optional.empty();
        String query = "SELECT id, titulo, descripcion FROM todolist WHERE id=?";

        try (Connection conn = connector.establecerConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ){
            ps.setInt(1, tareaId);
            ResultSet rs = ps.executeQuery();

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
        return result;
    }

    @Override
    public void modificarTarea(Tarea tarea) {
        String query = "UPDATE todolist SET titulo = ?, descripcion = ? WHERE id = ?";
        try ( Connection conn = connector.establecerConexion();
                PreparedStatement ps = conn.prepareStatement(query);
                ){
            ps.setString(1, tarea.getTitulo());
            ps.setString(2,tarea.getDescripcion());
            ps.setInt(3, tarea.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new CRUDException("Error al actualizar el producto con id:" + tarea.getId() + ": " + e.getMessage());
        }
    }

    @Override
    public void eliminarTareaPorId(Integer tareaId) {
        String query = "DELETE FROM todolist WHERE ID = ?";
        try ( Connection conn = connector.establecerConexion();
                PreparedStatement ps = conn.prepareStatement(query);
                ){
            ps.setInt(1, tareaId);
            ps.execute();
        } catch (SQLException e){
            throw new CRUDException("Error al eliminar tarea con id: " + tareaId + ": " + e.getMessage());
        }
    }
}
