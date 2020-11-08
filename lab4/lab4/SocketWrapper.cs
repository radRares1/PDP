using System.Net;
using System.Net.Sockets;
using System.Text;

namespace lab4
{
    public class SocketWrapper
    {
        public Socket sock = null; // socket to connect to server

        public const int BUFF_SIZE = 512; // 512 bytes 
        
        public byte[] buffer = new byte[BUFF_SIZE]; // buffer

        public StringBuilder responseContent = new StringBuilder();

        public int id; // index of obj
        public string hostname; // website address
        public string endpoint; // website navigation specifics

        public IPEndPoint remoteEndPoint; // ip of endpoint website
    }
}