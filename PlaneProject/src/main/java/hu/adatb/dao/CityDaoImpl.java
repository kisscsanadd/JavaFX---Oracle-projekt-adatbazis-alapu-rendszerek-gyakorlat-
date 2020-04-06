package hu.adatb.dao;

import hu.adatb.model.City;
import hu.adatb.query.Database;
import hu.adatb.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static hu.adatb.query.Queries.*;

public class CityDaoImpl implements  CityDao {

    @Override
    public boolean add(City city) {
        return false;
    }

    @Override
    public List<City> getAll() {
        List<City> result = new ArrayList<>();

        try {
            Statement stmt = Database.ConnectionToDatabaseWithStatement();

            ResultSet rs = stmt.executeQuery(SELECT_CITY);

            while (rs.next()) {
                City city = new City(
                        rs.getString("nev")
                );

                result.add(city);
            }

        } catch (SQLException | ClassNotFoundException e) {
            Utils.showWarning("Nem sikerült lekérni a város neveket");
        }

        return result;
    }
}