from django.urls import path
from tasks import views

urlpatterns = [
    path('', views.index, name='index'),
    path('tasks/run/', views.run_task, name='run_task'),
    path('tasks/status/<task_id>/', views.task_status, name='task_status'),
]
