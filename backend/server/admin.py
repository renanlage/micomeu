from django.contrib import admin
from server.models import Story, Rating, Vote

admin.site.register((Story, Rating, Vote))