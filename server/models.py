from django.db import models

class Story(models.Model):
	id = models.AutoField(primary_key=True)
	text = models.TextField()
	rating = models.IntegerField(default=0)

	class Meta:
		ordering = ['-rating']

	def __str__(self):
		return "story:{0} - r:{1}".format(self.id, self.rating)

	def to_json(self):
		return {
				'text': self.text,
				'rating': self.rating,
				}