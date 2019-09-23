package com.example.teammanagement.database;

public interface JConstants {

    String CONNECTION_URL =
            "jdbc:jtds:sqlserver://socialteamserver.database.windows.net:1433;" +
                    "DatabaseName=socialTeamDb;" +
                    "user;" +
                    "password;" +
                    "encrypt=true;" +
                    "trustServerCertificate=false;" +
                    "hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;";

    String INSERT_USER="INSERT INTO UTILIZATORI(NumeUtilizator,Email,Parola,Stare,Rol) VALUES(?,?,?,?,?)";
}
