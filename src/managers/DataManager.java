package managers;

import models.Coordinates;
import models.FuelType;
import models.Vehicle;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class CollectionManager {

    private String url = "jdbc:postgresql://pg:5432/studs";
    private String user;
    private String password;
    private Connection connection = null;

    private final LinkedList<Vehicle> collection = new LinkedList<Vehicle>();

    public synchronized LinkedList<Vehicle> getCollection(){
        return collection;
    }

    public CollectionManager(String user, String password) {
        this.user = user;
        this.password = password;
        url = "jdbc:postgresql://localhost:7777/studs";
        try {
            Class.forName("org.postgresql.Driver");

            Properties props = new Properties();
            props.setProperty("cachePrepStmts", "false");
            props.setProperty("useServerPrepStmts", "false");
            connection = DriverManager.getConnection(url, user, password);

            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS vehicle_users (" +
                    "username TEXT PRIMARY KEY, " +
                    "password TEXT" +
                    ");";
            statement.executeUpdate(sql);

            statement = connection.createStatement();
            sql = "CREATE SEQUENCE IF NOT EXISTS vehid\n" +
                    "    INCREMENT BY 1\n" +
                    "    START WITH 1\n" +
                    "    MINVALUE 1\n" +
                    "    NO MAXVALUE\n" +
                    "    CACHE 1;";
            statement.executeUpdate(sql);

            statement = connection.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS vehicles (" +
                    "id BIGINT DEFAULT nextval('vehid') PRIMARY KEY, " +
                    "name TEXT," +
                    "x INTEGER," +
                    "y INTEGER," +
                    "date DATE," +
                    "engine_power INTEGER," +
                    "number_of_wheels BIGINT," +
                    "fuel_consumption INTEGER," +
                    "fuel_type TEXT," +
                    "username TEXT" +
                    ");";
            statement.executeUpdate(sql);
            System.out.println("База данных подключена.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized boolean add(Vehicle e) {
        String sql = "INSERT INTO vehicles (name, x, y, date, engine_power, number_of_wheels, fuel_consumption, fuel_type, username) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING *";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, e.getName());
            preparedStatement.setInt(2, e.getCoordinates().getX());
            preparedStatement.setInt(3, e.getCoordinates().getY());
            preparedStatement.setDate(4, java.sql.Date.valueOf(e.getCreationDate()));
            preparedStatement.setInt(5, e.getEnginePower());
            preparedStatement.setLong(6, e.getNumberOfWheels());
            preparedStatement.setInt(7, e.getFuelConsumption());
            if (e.getFuelType() == null)
                preparedStatement.setString(8, "NULL");
            else
                preparedStatement.setString(8, e.getFuelType().toString());
            preparedStatement.setString(9, e.getUser());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                e.id = id;
                collection.add(e);
            }
            else
                return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return true;
    }

    public synchronized boolean insert(Vehicle e) {
        String sql = "INSERT INTO vehicles (id, name, x, y, date, engine_power, number_of_wheels, fuel_consumption, fuel_type, username) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING *";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(2, e.getName());
            preparedStatement.setInt(3, e.getCoordinates().getX());
            preparedStatement.setInt(4, e.getCoordinates().getY());
            preparedStatement.setDate(5, java.sql.Date.valueOf(e.getCreationDate()));
            preparedStatement.setInt(6, e.getEnginePower());
            preparedStatement.setLong(7, e.getNumberOfWheels());
            preparedStatement.setInt(8, e.getFuelConsumption());
            if (e.getFuelType() == null)
                preparedStatement.setString(9, "NULL");
            else
                preparedStatement.setString(9, e.getFuelType().toString());
            preparedStatement.setString(10, e.getUser());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                collection.add(e);
                return true;
            }
            else
                return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public synchronized String uniqueType() {
        Set<FuelType> fuelTypes = new HashSet<>();
        for (var e : collection)
            fuelTypes.add(e.getFuelType());
        StringBuilder answer = new StringBuilder();
        for (var a : fuelTypes)
            answer.append(a != null ? a.toString() : "null").append(" ");
        return answer.toString();
    }

    public synchronized boolean remove(long key, String author) {
        try {
            String sql = "DELETE FROM vehicles WHERE id = ? AND username = ?";
            PreparedStatement  preparedStatement = null;
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, key);
            preparedStatement.setString(2, author);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                for (Vehicle e : collection)
                    if (e.getId() == key) {
                        if (!author.equals(e.getUser()))
                            return false;
                        if (rowsAffected > 0) {
                            collection.remove(e);
                            break;
                        }
                    }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return false;
    }

    public synchronized boolean removeLower(long key, String author) {
        try {
            String sql = "DELETE FROM vehicles WHERE id < ? AND username = ?";
            PreparedStatement  preparedStatement = null;
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, key);
            preparedStatement.setString(2, author);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                for (Vehicle e : collection)
                    if (e.getId() < key) {
                        if (!author.equals(e.getUser()))
                            return false;
                        collection.remove(e);
                    }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return false;
    }

    public synchronized Vehicle getById(Long id) {
        for (Vehicle e : collection)
            if (e.getId() == id)
                return e;
        return null;
    }

    public synchronized void clearCollection(String author) {
        try {
            String sql = "DELETE FROM vehicles WHERE username = ?";
            PreparedStatement  preparedStatement = null;
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, author);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                this.collection.removeIf(a -> a.getUser().equals(author));
            }
            System.out.println(rowsAffected);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public synchronized void saveCollection() {
        System.out.println("Изменения сохраняются автоматически.");
    }

    public synchronized void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public synchronized boolean loadCollection() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM vehicles");


            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                int enginePower = resultSet.getInt("engine_power");
                long wheels = resultSet.getInt("number_of_wheels");
                int consumption = resultSet.getInt("fuel_consumption");
                String fuelString = resultSet.getString("fuel_type");
                FuelType fuel;
                if (fuelString.equals("NULL"))
                    fuel = null;
                else
                    fuel = FuelType.valueOf(fuelString);
                String user = resultSet.getString("username");
                Vehicle v = new Vehicle(id, name, new Coordinates(x, y), date, enginePower, wheels, consumption, fuel, user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Коллекция загружена.");
        return true;
    }

    public synchronized List<Vehicle> filter(Function<Vehicle, Boolean> validator) {
        List<Vehicle> out = collection.stream().filter(e -> validator.apply(e)).sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).toList();
        /*Collections.sort(out, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle o1, Vehicle o2) {
                System.out.println(2);
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });*/
        //if (out.size() <= 1)
        //    return out;
        //out.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return out;
    }

    public static String showList(List<Vehicle> collection) {
        if (collection.isEmpty()) return "Элементы не найдены!";
        StringBuilder info = new StringBuilder();
        for(var e: collection)
            info.append(e.getId()).append(" ").append(e.toString()).append("\n");
        return info.toString().trim();
    }
}
