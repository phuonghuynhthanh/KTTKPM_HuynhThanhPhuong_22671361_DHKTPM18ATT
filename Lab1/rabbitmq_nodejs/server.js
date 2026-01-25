const express = require("express");
const jwt = require("jsonwebtoken");

const app = express();
app.use(express.json());

const PORT = 4000;
const SECRET_KEY = "my_secret_key";
const TOKEN_EXPIRATION = "60s";
const TOKEN_ALGORITHM = "HS256";

const verifyToken = (req, res, next) => {
  const authHeader = req.headers["authorization"];
  const token = authHeader && authHeader.split(" ")[1];

  if (!token) {
    return res.status(401).json({
      error: "Truy cập bị từ chối!",
      detail: "Bạn chưa gửi Token (Vui lòng đăng nhập để lấy Token)",
    });
  }

  jwt.verify(
    token,
    SECRET_KEY,
    { algorithms: [TOKEN_ALGORITHM] },
    (err, decodedUser) => {
      if (err) {
        return res.status(403).json({
          error: "Token không hợp lệ!",
          detail: "Token này là giả hoặc đã hết hạn sử dụng.",
        });
      }

      req.user = decodedUser;
      next();
    },
  );
};

app.post("/login", (req, res) => {
  const { username } = req.body;

  if (!username) {
    return res.status(400).json({ error: "Vui lòng nhập username" });
  }

  const userPayload = { name: username, role: "sinh_vien" };
  const accessToken = jwt.sign(userPayload, SECRET_KEY, {
    expiresIn: TOKEN_EXPIRATION,
    algorithm: TOKEN_ALGORITHM,
  });

  console.log(`[LOGIN] Đã cấp token cho user: ${username}`);

  res.json({
    message: "Đăng nhập thành công!",
    accessToken: accessToken,
  });
});

app.get("/decode", verifyToken, (req, res) => {
  console.log("[DECODE] Đã giải mã token thành công");

  const decodedInfo = {
    header: jwt.decode(req.headers["authorization"].split(" ")[1], {
      complete: true,
    }).header,
    payload: req.user,
  };

  res.json({
    status: "Success",
    message: "Đây là thông tin gốc được giải mã từ Token:",
    thong_tin_giai_ma: decodedInfo,
  });
});

app.listen(PORT, () => {
  console.log(`Server JWT đang chạy tại: http://localhost:${PORT}`);
});
