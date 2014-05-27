from django.db import models

class Story(models.Model):
	text = models.TextField()
	rating = models.IntegerField(default=0)

	class Meta:
		ordering = ['-rating']