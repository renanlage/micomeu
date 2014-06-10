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
	story = models.OneToOneField(Story)
	score = models.IntegerField()
	nratings = models.IntegerField()

	def add(self, new_rating):
		new_score = self.score * self.nratings
		new_score += float(new_rating)
		self.nratings += 1
		self.score = round(new_score / self.nratings)

	def __str__(self):
		return "rating of {0} = {1}".format(self.story.id, self.score)

