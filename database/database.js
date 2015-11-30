
/** users indexes **/
db.getCollection("users").ensureIndex({
  "_id": NumberInt(1)
},[
  
]);

/** users records **/
db.getCollection("users").insert({
  "_id": ObjectId("565c667f59eb867d4384ba8e"),
  "username": "admin",
  "password": "",
  "role": 1
});
