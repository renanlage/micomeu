 # -*- coding: utf-8 -*-
import os

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "micomeu.settings")
from django.conf import settings

from server.models import Story, Rating

story = Story(text = "Esta semana resolvi virar garota de programa por causa da situação financeira difícil. Não sou lésbica, mas aceitei quando uma garota ligou marcando o meu primeiro programa. Chegando no local combinado, descubro que era minha irmã.")
story.save()
Rating(story = story, score = 9, nratings = 12).save()

story = Story(text = "Há dois anos eu esperava pro meu pai comprar um notebook novo pra mim, ontem eu levei ele pra escola pra mostrar pros eu amigos, na volta eu fui assaltado")
story.save()
Rating(story = story, score = 6, nratings = 8).save()

story = Story(text = "Hoje é meu aniversario e logo de manha fui trabalhar como outro dia normal, quando estava no final da minha rua um zé ruela veio com uma faca pra me assaltar... roubou meu celular, só que ontem eu tinha feito backup das informações do pc do meu trampo, pro tecnico restaurar o sistema ou algo assim, que iria apagar tudo! Adivinha aonde eu fiz o backup: Na @#$%#$ do celular!")
story.save()
Rating(story = story, score = 4, nratings = 2).save()

story = Story(text = "Há x dias eu terminei com meu namorado. A amiga dele disse que ele ainda gostava de mim e que nos ajudaria a voltar e tal. Bom, recebi um sms dele comendo ela por trás.")
story.save()
Rating(story = story, score = 7, nratings = 20).save()

story = Story(text = "Hoje cheguei mais cedo da escola e descobri que meus pais realmente vão se separar. Acabei de ouvir os dois quase se matando na cozinha. Eles estão discutindo porquê nenhum dos dois quer ficar comigo.")
story.save()
Rating(story = story, score = 9, nratings = 1).save()