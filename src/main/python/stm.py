#!/home/kiryushin/projects/python/stm/venv/lib/python3.6
from todoist.api import TodoistAPI
import json

api = TodoistAPI('4b6a0663cdf468b62a69c852b72f851782147daf')
api.sync()
print(api.state['projects'])

f=open('/home/kiryushin/projects/BotSTMApi/src/main/resources/text/test')
for line in f.readlines():
    answer = line.split("|")

print(answer[0])
print(answer[1])
print(answer[2])

task1=api.items.add(answer[0], 2206180705)

comment1=api.notes.add(task1['id'],answer[1])

obj=api.uploads.add('/home/kiryushin/projects/BotSTMApi/src/main/resources/pictures/'+answer[2]+'.jpeg')



print('/home/kiryushin/projects/BotSTMApi/src/main/resources/pictures/'+answer[2]+'.jpeg')

obj1=api.uploads.get()


print(obj1[0])

#obj1.clear();
comment1=api.notes.add(task1['id'],' ',file_attachment=obj1[0])

api.commit(),