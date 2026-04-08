from flask import Flask

app = Flask(__name__)


@app.get("/")
def home():
    return "Flask service behind Traefik reverse proxy"


@app.get("/health")
def health():
    return {"status": "ok"}


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
