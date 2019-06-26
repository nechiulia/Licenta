package com.example.teammanagement.database;

public interface JConstants {

    String CONNECTION_URL =
            "jdbc:jtds:sqlserver://socialteamserver.database.windows.net:1433;" +
                    "DatabaseName=socialTeamDb;" +
                    "user=socialteam-admin@socialteamserver;" +
                    "password=1Q2w3e4r;" +
                    "encrypt=true;" +
                    "trustServerCertificate=false;" +
                    "hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;";
}
