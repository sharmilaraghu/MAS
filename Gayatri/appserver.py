#!/usr/bin/python
import sys, socket, json, xmpp, random, string, threading, time
from json import JSONEncoder
from threading import *

SERVER = 'gcm.googleapis.com'
PORT = 5235
USERNAME = "732911902235"
PASSWORD = "AIzaSyAL3yGGs3E0umjqlHB2StL9xcv01KWbQy4"
REGISTRATION_ID = "Registration Id of the target device"

unacked_messages_quota = 100
send_queue = []
lock = threading.Lock()
		
# Return a random alphanumerical id
def random_id():
  rid = ''
  for x in range(8): rid += random.choice(string.ascii_letters + string.digits)
  return rid
		
def pass_to_back_end(message):
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	server_address = ('localhost',39999)
	sock.connect(server_address)
	msg = JSONEncoder().encode(message)
	try:
		sock.sendall(msg + '\n')
	finally:
		sock.close()	

def message_callback(session, message):
  global unacked_messages_quota
  gcm = message.getTags('gcm')
  if gcm:
    gcm_json = gcm[0].getData()
    msg = json.loads(gcm_json)
    if not msg.has_key('message_type'):
      # Acknowledge the incoming message immediately.
      send({'to': msg['from'],
            'message_type': 'ack',
            'message_id': msg['message_id']})
      # Queue a response back to the server.
      if msg.has_key('from'):
        # Send a request to the backend java for getting updated time.
        message = msg['data']
        message['from'] = msg['from']
        t = threading.Thread(target=pass_to_back_end, args=(message,))
        t.start()
    elif msg['message_type'] == 'ack' or msg['message_type'] == 'nack':
      unacked_messages_quota += 1

def send(json_dict):
  template = ("<message><gcm xmlns='google:mobile:data'>{1}</gcm></message>")
  client.send(xmpp.protocol.Message(
      node=template.format(client.Bind.bound[0], json.dumps(json_dict))))

def flush_queued_messages():
	global unacked_messages_quota
	while len(send_queue) and unacked_messages_quota > 0:
		with lock:
			send(send_queue.pop(0))
			unacked_messages_quota -= 1

client = xmpp.Client('gcm.googleapis.com', debug=['socket'])
client.connect(server=(SERVER,PORT), secure=1, use_srv=False)
auth = client.auth(USERNAME, PASSWORD)
if not auth:
  print 'Authentication failed!'
  sys.exit(1)

client.RegisterHandler('message', message_callback)

send_queue.append({'to': REGISTRATION_ID,
                   'message_id': 'reg_id',
                   'data': {'message_destination': 'RegId',
                            'message_id': random_id()}})



server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(('localhost', 40000))
server.listen(5)	
	
def oneWay():
	while True:
		client.Process(1)
		flush_queued_messages()

def recv_timeout(the_socket,timeout=2):
	#make socket non blocking
	the_socket.setblocking(0)
	#total data partwise in an array
	total_data=[];
	data='';
	#beginning time
	begin=time.time()
	while 1:
		#if you got some data, then break after timeout
		if total_data and time.time()-begin > timeout:
			break
		#if you got no data at all, wait a little longer, twice the timeout
		elif time.time()-begin > timeout*2:
			break
		#recv something
		try:
			data = the_socket.recv(8192)
			if data:
				total_data.append(data)
				#change the beginning time for measurement
				begin = time.time()
			else:
				#sleep for sometime to indicate a gap
				time.sleep(0.1)
		except:
			pass
     
	#join all parts to make final string
	return ''.join(total_data)
      
def anotherWay():
	while True:
		print 'test'
		conn, addr =  server.accept()	
		#data = conn.recv(4096)
		data = recv_timeout(conn)
		if data:
			msg = json.loads(data)
			print "update alarm is " + str(msg['update_alarm']) + "alarm id is " + str(msg['salarm_id'])
			with lock:
				send_queue.append({'to': msg['from'],'message_id': random_id(),'data': {'update_alarm': msg['update_alarm'],'salarm_id': msg['salarm_id']}})			
		conn.close()	

s = threading.Thread(target=oneWay)
s.start()
t = threading.Thread(target=anotherWay)
t.start()		 

  
  