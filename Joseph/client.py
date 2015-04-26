import socket
import sys
from json import JSONEncoder

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect the socket to the port where the server is listening
server_address = ('localhost', 39999)
print >>sys.stderr, 'connecting to %s port %s' % server_address
sock.connect(server_address)

try:
    jsonString = JSONEncoder().encode({
            "dest_lon" : -84.4281,
            "dest_lat" : 33.6367,
            "src_lon" : -80.20202,
            "src_lat" : 33.3333,
            "alarm_time" : 21600,
            "from" : "WHY_JOSEPH_IS_SO_COOL",
            "salarm_id" : "SAlarm id"
        })
    # Send data
    message = '\n'
    print >>sys.stderr, 'sending "%s"' % jsonString
    sock.sendall(jsonString + message)

    # # Look for the response
    # amount_received = 0
    # amount_expected = len(message)
    
    # while amount_received < amount_expected:
    #     data = sock.recv(16)
    #     amount_received += len(data)
    #     print >>sys.stderr, 'received "%s"' % data

finally:
    print >>sys.stderr, 'closing socket'
    sock.close()
