const express = require("express");
const mongoose = require("mongoose");

const app = express();
app.use(express.json());

// Kết nối tới MongoDB (URL sử dụng tên service 'db' trong docker-compose)
const MONGO_URI = "mongodb://db:27017/mydatabase";

mongoose
  .connect(MONGO_URI)
  .then(() => console.log("Đã kết nối tới MongoDB thành công!"))
  .catch((err) => console.error("Lỗi kết nối MongoDB:", err));

// Tạo Schema và Model đơn giản
const UserSchema = new mongoose.Schema({ name: String, role: String });
const User = mongoose.model("User", UserSchema);

// REST API Routes
app.get("/users", async (req, res) => {
  try {
    const users = await User.find();
    res.json(users);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
});

app.post("/users", async (req, res) => {
  try {
    const newUser = new User(req.body);
    await newUser.save();
    res.status(201).json(newUser);
  } catch (err) {
    res.status(400).json({ message: err.message });
  }
});

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Node.js App đang chạy trên port ${PORT}`);
});
