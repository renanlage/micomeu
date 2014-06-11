from django.db import models

class Story(models.Model):
	id = models.AutoField(primary_key=True)
	text = models.TextField()

	def __str__(self):
		return "story:{0}".format(self.id)

	def to_json(self):
		return {
				"id": self.id,
				"text": self.text,
				}

class Rating(models.Model):
	label = models.CharField(max_length = 200, unique = True)
	value = models.IntegerField(unique = True)

	def __str__(self):
		return "{0} - {1}".format(self.label, self.value)

class Vote(models.Model):
	rating = models.ForeignKey(Rating)
	story = models.ForeignKey(Story)

	def __str__(self):
		return "rating of {0} = {1}".format(self.story.id, self.rating.value)

