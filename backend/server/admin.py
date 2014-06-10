from django.contrib import admin
from server.models import Story, Rating

admin.site.register((Story, Rating))