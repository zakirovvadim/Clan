This application demonstrate of using R2DBC framework for reactive web service. Info occurs to H2 database.

1. Endpoints:
1.1 http://localhost:8080/clan (POST) make new clan;
json body:
{
"name":"Montegra4",
"gold":250
}
1.2 http://localhost:8080/clan/{id} (GET) get clan by id;
1.3 http://localhost:8080/clans (GET) find all clans;
1.4 http://localhost:8080/plusGold (PUT) add the gold for the clan by Id;
json body:
{
"id":1,
"name":"Citrus",
"gold":1
}
1.5 http://localhost:8080/minusGold (PUT) subtract the gold for the clan by Id;
json body:
{
"id":1,
"name":"Citrus",
"gold":1
}

2. Entities:
Clan - general entity with data about money balance;
History - auxiliary class for save info about operation under clan.
