const express = require("express");
const mysql = require("mysql2");

const app = express();
const PORT = 3000;

const DB_HOST = process.env.DB_HOST || "mysql";
const DB_USER = process.env.DB_USER || "root";
const DB_PASSWORD = process.env.DB_PASSWORD || "rootpassword";
const DB_NAME = process.env.DB_NAME || "mydb";

let db;
let isDbReady = false;

function createDbConnection() {
  return mysql.createConnection({
    host: DB_HOST,
    user: DB_USER,
    password: DB_PASSWORD,
    database: DB_NAME,
  });
}

function connectWithRetry() {
  db = createDbConnection();
  db.connect((err) => {
    if (err) {
      isDbReady = false;
      console.error("DB connection failed, retrying in 3s:", err.message);
      setTimeout(connectWithRetry, 3000);
      return;
    }
    isDbReady = true;
    console.log("Connected to MySQL!");
  });
}

// API test
app.get("/", (req, res) => {
  if (!db || !isDbReady) {
    return res.status(503).send("Database is not ready");
  }
  db.query("SELECT 1 + 1 AS result", (err, results) => {
    if (err) return res.send("DB query error");
    res.send(`Result: ${results[0].result}`);
  });
});

connectWithRetry();

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
