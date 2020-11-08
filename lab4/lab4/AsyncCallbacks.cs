using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace lab4
{
    public static class AsyncCallbacks
    {
        //wrap function
        //for each of the url, start the callbacks
        public static void run(List<string> links)
        {
            for (var i = 0; i < links.Count; i++)
            {
                startCallBacks(links[i], i);
                Thread.Sleep(2000);
            }
        }

        public static void startCallBacks(string link, int id)
        {
            var linkDns = Dns.GetHostEntry(link.Split('/')[0]);
            var ipAddrs = linkDns.AddressList[0];
            var remoteEndPoint = new IPEndPoint(ipAddrs, HTTPParser.PORT);

            var socket = new Socket(ipAddrs.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            //if there is an endpoint we set it
            var endpoint = String.Empty;
            if (link.Contains("/"))
            {
                endpoint = link.Substring(link.IndexOf("/"));
            }
            else
            {
                endpoint = "/";
            }
            
            //create the socket wrapper since we needed more info than just the socket
            var socketWrap = new SocketWrapper()
            {
                sock = socket,
                hostname = link.Split('/')[0],
                endpoint = endpoint,
                remoteEndPoint = remoteEndPoint,
                id = id
            };

            //we begin the connection
            socket.BeginConnect(remoteEndPoint, startConnect, socketWrap);
        }

        /*
         * function called by the begin connect async callback
         * ar is the socket wrapper
        */
        public static void startConnect(IAsyncResult ar)
        {
            //we deserialize the wrapper and get the info
            var socketWrap = (SocketWrapper) ar.AsyncState;
            var socket = socketWrap.sock;
            var id = socketWrap.id;
            var hostname = socketWrap.hostname;

            socket.EndConnect(ar);

            System.Console.WriteLine("Socket {0} connected to {1} (ip:{2})", id, hostname, socket.RemoteEndPoint);

            //get the request that we want to make as a string and
            //convert it to bytes so we can send it with BeginSend

            var requestToBytes = Encoding.ASCII.GetBytes(HTTPParser.getRequest(hostname, socketWrap.endpoint));

            //start sending the request as bytes
            socket.BeginSend(requestToBytes, 0, requestToBytes.Length, 0, startSend, socketWrap);
        }

        /*
         * function called by the begin send async callback
         * ar is the socket wrapper
        */
        public static void startSend(IAsyncResult ar)
        {
            var socketWrap = (SocketWrapper) ar.AsyncState;
            var socket = socketWrap.sock;
            var id = socketWrap.id;

            //we get the respone
            var responseInBytes = socket.EndSend(ar);
            Console.WriteLine("{0} sent {1} bytes to server.", id, responseInBytes);

            //and start receiving
            socket.BeginReceive(socketWrap.buffer, 0, 512, 0, startReceive, socketWrap);
        }

        /*
         * function called by the begin receive async callback
         * ar is the socket wrapper
        */
        public static void startReceive(IAsyncResult ar)
        {
            var socketWrap = (SocketWrapper) ar.AsyncState;
            var socket = socketWrap.sock;
            var id = socketWrap.id;

            //Console.WriteLine("yea");

            try
            {
                var requestFromStart = socket.EndReceive(ar);

                //we unwrap the respons from the bytes recevied and append them to the content container
                socketWrap.responseContent.Append(Encoding.ASCII.GetString(socketWrap.buffer, 0, requestFromStart));
                //Console.WriteLine(Encoding.ASCII.GetString(socketWrap.buffer, 0, requestFromStart));

                //we re-try if we didn't get a respone
                if (!HTTPParser.gotResponseHeader(socketWrap.responseContent.ToString()))
                {
                    socket.BeginReceive(socketWrap.buffer, 0, 512, 0, startReceive, socketWrap);
                    //Console.WriteLine("yea");
                }


                //Console.WriteLine("yea");
                var responseBody = HTTPParser.getResponseBody(socketWrap.responseContent.ToString());
                var contentLenght = HTTPParser.getContentLen(socketWrap.responseContent.ToString());


                //also we keep receiving if we didn't finish reading
                if (responseBody.Length < contentLenght) 
                {
                    socket.BeginReceive(socketWrap.buffer, 0, 512, 0, startReceive, socketWrap);
                    
                }
                //we print everything since there is nothing more to receive
                else
                {
                    
                    foreach (var i in socketWrap.responseContent.ToString().Split('\r', '\n'))
                        System.Console.WriteLine(i);

                    System.Console.WriteLine("length is " + contentLenght);

                    socket.Shutdown(SocketShutdown.Both);
                    socket.Close();
                }
            }
            catch (Exception e)
            {
                System.Console.WriteLine(e.ToString());
            }
        }
    }
}