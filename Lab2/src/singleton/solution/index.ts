class Logger {
  private static instance: Logger;

  private constructor() {
    console.log("Khởi tạo Logger instance mới!");
  }

  public static getInstance(): Logger {
    if (!Logger.instance) {
      Logger.instance = new Logger();
    }
    return Logger.instance;
  }

  public log(message: string): void {
    const timestamp = new Date().toISOString();
    console.log(`[INFO] ${timestamp} - ${message}`);
  }

  public error(message: string): void {
    const timestamp = new Date().toISOString();
    console.error(`[ERROR] ${timestamp} - ${message}`);
  }
}

const logger1 = Logger.getInstance();
logger1.log("Ứng dụng bắt đầu chạy.");

const logger2 = Logger.getInstance();
logger2.error("Không tìm thấy dữ liệu.");

console.log("logger1 === logger2:", logger1 === logger2);
