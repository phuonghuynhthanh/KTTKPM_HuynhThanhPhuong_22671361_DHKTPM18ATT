import time
from celery import shared_task


@shared_task(bind=True)
def process_long_task(self, task_name, duration=5):
    """
    Xử lý task dài - mô phỏng công việc tốn thời gian.
    """
    for i in range(duration):
        time.sleep(1)
        self.update_state(
            state='PROGRESS',
            meta={'current': i + 1, 'total': duration, 'status': f'Processing {i + 1}/{duration}'}
        )
    return {'status': 'completed', 'result': f'{task_name} finished after {duration} seconds'}


@shared_task
def send_notification(message):
    """
    Gửi notification - mô phỏng việc gửi email/notification.
    """
    time.sleep(2)
    return f'Notification sent: {message}'


@shared_task
def generate_report(report_type):
    """
    Tạo report - mô phỏng việc tạo báo cáo.
    """
    time.sleep(3)
    return f'{report_type} report generated successfully'
