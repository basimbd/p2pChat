# p2pChat
<p>This 'p2pChat'app is developed to connect two android platform devices.
It works by establishing a Peer-to-Peer connection between the two devices.
All the basic features such as exchanging messages, simple text files, saving
conversations and changing backgrounds are implemented with some additional
features.</p>
<h1>Working Principle</h1>
<p>Working principle for this app is very simple and straight forward. One device creates a SocketServer
with a port address and waits for connection from the other device. The other device then connects
to the device by using the specific port address and the ip address. Once the connection is 
established both the device can send and receive messages and files.</p>
<h1>Instructions to use the app</h1>
<p>- Open the app in two android devices. Device 'A' and 'B'
<p>- In device 'A' click on 'CREATE SERVER', and in 'B' device click 'CONNECT'</p>
<p>- Give port address and click 'START SERVER' in 'A', it will show the specified port and ip address of device 'A'</p>
<p>- Give the ip address and port number that were showed in device 'A' as inputs in device 'B' and click 'CONNECT'</p>
<p>- Click on 'ENTER CHAT THREAD' from both the devices. But make sure you enter chat thread from 'A' device first. Because device 'A' is creating the server and waiting for connection, so it must be opened before 'B' device can request to connect.</p>
<p>- Chat with your friends and share files as long as you like.</p>
<h1>Additional Features</h1>
<p>- User can use 'Light' or 'Dark Theme' according to their wish in runtime.</p>
<p>- Different Button and Text Colors are added in different themes.</p>
<p>- Messenger like interface for better user experience.</p>
<p>- Time of sending the message is also showed.</p>
