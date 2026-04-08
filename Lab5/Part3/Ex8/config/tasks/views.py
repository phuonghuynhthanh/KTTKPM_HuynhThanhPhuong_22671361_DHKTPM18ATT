from django.shortcuts import render
from django.http import JsonResponse
from celery.result import AsyncResult
from .tasks import process_long_task, send_notification, generate_report


def index(request):
    return render(request, 'index.html')


def run_task(request):
    task_type = request.GET.get('type', 'long')
    
    if task_type == 'long':
        task = process_long_task.delay('Background Job', duration=10)
    elif task_type == 'notification':
        task = send_notification.delay('Hello from Celery!')
    elif task_type == 'report':
        task = generate_report.delay('Monthly Summary')
    else:
        task = process_long_task.delay('Unknown Job', duration=5)
    
    return JsonResponse({
        'task_id': task.id,
        'status': 'submitted',
        'message': f'Task {task.id} submitted to queue'
    })


def task_status(request, task_id):
    result = AsyncResult(task_id)
    
    response = {
        'task_id': task_id,
        'status': result.state,
    }
    
    if result.ready():
        response['result'] = result.result
    elif result.state == 'PROGRESS':
        response['progress'] = result.info
    
    return JsonResponse(response)
