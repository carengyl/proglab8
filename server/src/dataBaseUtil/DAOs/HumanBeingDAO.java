package dataBaseUtil.DAOs;

import entities.*;

public class HumanBeingDAO {
    public static final String HumanBeingTableName = "s368587HumanBeings";
    public static final String HumanBeingSequenceName = "s368587HumanBeings_id_seq";

    public static String getInitIdSequenceStatement() {
        return "CREATE SEQUENCE IF NOT EXISTS" + HumanBeingSequenceName + "INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1";
    }

    public static String getInitTableStatement() {
        return "CREATE TABLE IF NOT EXISTS" + HumanBeingTableName +
                "(" +
                "key bigint NOT NULL PRIMARY KEY" +
                "id bigint NOT NULL DEFAULT nextval('" + HumanBeingSequenceName + "')," +
                "creationDate date NOT NULL," +
                "name varchar(100) NOT NULL CHECK(name<>'')," +
                "x int NOT NULL," +
                "y int NOT NULL CHECK (y >= " + Coordinates.getMIN_Y() + ")," +
                "realHero boolean NOT NULL," +
                "hasToothPick boolean NOT NULL," +
                "impactSpeed float8 CHECK(impactSpeed > " + HumanBeing.getMinImpactSpeed() +
                "OR impactSpeed = NULL)," +
                "minutesOfWaiting float8," +
                "weaponType varchar(50) CHECK(weaponType = " + WeaponType.AXE  +
                "OR weaponType = " + WeaponType.PISTOL +
                "OR weaponType = " + WeaponType.RIFLE +
                "OR weaponType = " + WeaponType.SHOTGUN +
                "OR weaponType = " + WeaponType.MACHINE_GUN + ")," +
                "mood varchar(50) CHECK(mood = " + Mood.LONGING +
                "OR mood = " + Mood.CALM +
                "OR mood = " + Mood.RAGE +
                "OR mood = " + Mood.FRENZY + ")," +
                "carName varchar(100)," +
                "carCost int CHECK (carCost > 0" +
                "OR carCost = NULL)," +
                "carHorsePowers int CHECK (carHorsePowers > 0" +
                "OR carHorsePowers = NULL)," +
                "carCarBrand varchar(50) CHECK (carCarBrand = " + CarBrand.COOL_BRAND +
                "OR carCarBrand = " + CarBrand.NOT_COOL_BRAND +
                "OR carCarBrand = NULL)," +
                "owner_id bigint NOT NULL REFERENCES " + UsersDAO.UsersTableName + "(id)" +
                ");";
    }
}
