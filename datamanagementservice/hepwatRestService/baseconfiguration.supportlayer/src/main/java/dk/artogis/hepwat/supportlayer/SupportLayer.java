package dk.artogis.hepwat.supportlayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.artogis.hepwat.common.database.Connection;
import dk.artogis.hepwat.common.database.SqlCriteria;
import dk.artogis.hepwat.common.utility.Status;
//import dk.artogis.hepwat.datastore.DataStore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.log4j.Logger;


public class SupportLayer {

    @JsonIgnore
    private static String tableName = "config_support_layer";
    private Integer id ;
    private String name;
    private String wfs;
    private Integer zOrder;

    @JsonIgnore
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static String f_id = "id";
    private static String f_name = "name";
    private static String f_wfs = "wfs";
    private static String f_z_order = "z_order";

    private static String f_componentTableName = "support_layer_table_name" ;

    //region getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWfs() {
        return wfs;
    }

    public void setWfs(String wfs) {
        this.wfs = wfs;
    }

    public Integer getzOrder() {
        return zOrder;
    }

    public void setzOrder(Integer zOrder) {
        this.zOrder = zOrder;
    }

    //endregion getters and setters
    public SupportLayer(Integer  id, String name, String wfs, Integer zOrder)
    {
        this.id = id;
        this.name = name;
        this.wfs = wfs;
        this.zOrder = zOrder;
    }

    public SupportLayer()
    {

    }

    public SupportLayer(ResultSet rs)
    {
        //TODO : fill out if nessecary
    }

    /*
    public Status Insert(Connection connection )
    {
        Status status = new Status();
        status.Success = false;
        String[] insertFields = null;
        String[] insertValues = null;
        Map parameters = null;

        insertFields = getInsertFields();
        insertValues = getInsertValuesParameters();
        parameters = new HashMap();

        if (this.id == null)
            this.id = UUID.randomUUID();
        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_wfs, this.wfs);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+ f_z_order, this.zOrder);

        Integer result = connection.insert(tableName, insertFields, insertValues, parameters, false, null);

        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows updated, or only part of records updated";
        return status;
    }
    */

    /*
    public Status Update(Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        String[] updateFields = getFields();
        String[] updateValues = getValuesParameters();
        Map parameters = null;

        updateFields = getUpdateFields();
        updateValues = getUpdateValuesParameters();
        parameters = new HashMap();

        parameters.put("@"+f_id, this.id);
        parameters.put("@"+f_wfs, this.wfs);
        parameters.put("@"+f_name, this.name);
        parameters.put("@"+ f_z_order, this.zOrder);

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@" + f_id;
        criterias[0] = criteria1;

        Integer result = connection.update(tableName, updateFields, updateValues, criterias, parameters);

        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows updated, or only part of records updated";
        return status;
    }
    */

    /*
    public static Status Delete(UUID id, Connection connection)
    {
        Status status = new Status();
        status.Success = false;
        Map parameters = new HashMap();
        parameters.put("@"+f_id, id);

        SqlCriteria[] criterias = new SqlCriteria[1];
        SqlCriteria criteria1 = new SqlCriteria();
        criteria1.FieldName = f_id;
        criteria1.CompareOperator = "=";
        criteria1.FieldValue = "@"+f_id;

        criterias[0] = criteria1;

        Integer result = connection.delete(tableName, criterias, parameters);
        if (result == 1 )
            status.Success = true;
        else
            status.Message = "no rows deleted";
        return status;
    }
    */

    public static SupportLayer GetSupportLayer(Integer id, Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};

        String[] fields = getFields();

        SqlCriteria aCriteria = new SqlCriteria();
        aCriteria.FieldName = f_id;
        aCriteria.CompareOperator = "=";
        aCriteria.FieldValue = "@f_id";
        SqlCriteria[] criterias = new SqlCriteria[1];
        criterias[0] = aCriteria;

        Map parameters = new HashMap();
        parameters.put("@f_id", id);
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, criterias, parameters, null, 0);
        }
        catch (Exception ex)
        {
            throw new Exception("error in finding resultset");
        }
        List<SupportLayer> supportLayers = GetSupportLayersFromResultset(rs);
        if (supportLayers.size() >= 1) {
            SupportLayer sl = (SupportLayer) (supportLayers.toArray())[0];
            return sl;
        }
        return  null;
    }

    private static String[] getUpdateFields() {
        return new String[]{f_name, f_componentTableName, f_wfs, f_z_order};
    }

    private static String[] getUpdateValuesParameters() {
        return new String[]{"@"+f_name, "@"+f_componentTableName, "@"+f_wfs, "@"+ f_z_order};
    }

    private static String[] getFields() {
        return new String[]{f_id, f_name, f_wfs, f_z_order};
    }

    private static String[] getValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name, "@"+f_componentTableName, "@"+f_wfs, "@"+ f_z_order};
    }


    private static String[] getInsertFields() {
        return new String[]{f_id, f_name, f_componentTableName, f_wfs, f_z_order};
    }

    private static String[] getInsertValuesParameters() {
        return new String[]{"@"+f_id, "@"+f_name, "@"+f_componentTableName, "@"+f_wfs, "@"+ f_z_order};
    }
    public static List<SupportLayer> GetSupportLayers(Connection connection ) throws Exception
    {
        String[] tableNames = new String[]{tableName};
        //System.out.println("GetSupportLayers - tableName[0] " + tableNames[0]);

        String[] fields = getFields();

        for(int i = 0; i < fields.length; i++) {
            //System.out.println("GetSupportLayers - field " + (i + 1) + ": " + fields[i]);
        }
        Map parameters = new HashMap();
        ResultSet rs = null;

        try {
            rs = connection.select(tableNames, fields, null, null, null, 0);
            //System.out.println("GetSupportLayers - result set: " + rs);
        }
        catch (Exception ex) {
            throw new Exception("error in finding resultset");
        }

        List<SupportLayer> supportLayers = null;

        try {
            supportLayers = GetSupportLayersFromResultset(rs);
        }
        catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

        return  supportLayers;
    }

    public static List<SupportLayer> GetSupportLayersFromResultset(ResultSet resultSet) throws Exception {
        List<SupportLayer> supportLayers = new ArrayList<SupportLayer>();
        if (resultSet == null) {
            throw new Exception("resultset null");
        }

        try
        {
            while(resultSet.next()) {
                SupportLayer componentType = new SupportLayer();
                componentType.id = resultSet.getInt(f_id);
                componentType.name = resultSet.getString(f_name);
                componentType.wfs = resultSet.getString(f_wfs);
                componentType.zOrder = resultSet.getInt(f_z_order);

                supportLayers.add(componentType);
            }
        }
        catch (SQLException ex)
        {
            //TODO : Logging
            throw new Exception("could not retrieve data from resultset");
        }
        return supportLayers;
    }



}
