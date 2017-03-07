package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

	public static void main(String[] args) throws IOException {
		
		ServerSocket serverSoket = new ServerSocket(9999);
		LinkedList<ServerNit> klijenti = new LinkedList<ServerNit>();
		//klijenti = null;
		
		//DatagramSocket datagramSoket = new DatagramSocket(10000);
		
		//byte[] podaciOdKlijenta = new byte[1024];
		//byte[] podaciZaKlijenta = new byte[1024];
		
		while(true){
			
			String ime = null;
			String pol = null;
			boolean zensko;
			//InetAddress IPAdresa;
			//int port;
			
			Socket soketZaKomunikaciju = serverSoket.accept();
			
			
			//ulazni tok od klijenta
			BufferedReader ulazniTokOdKlijenta = new BufferedReader
					(new InputStreamReader
							(soketZaKomunikaciju.getInputStream()));
			
			//izlazni tok ka klijentu
			PrintStream izlazniTokKaKlijentu = new PrintStream
					(soketZaKomunikaciju.getOutputStream());
			
			
			//poruka dobrodoslice i povezivanja sa serverom
			izlazniTokKaKlijentu.println("Uspesno ste se povezali u pricaonicu.");

			
			//dodavanje klijenta
			ServerNit novi = new ServerNit(soketZaKomunikaciju, klijenti);
			klijenti.add(novi);
			novi.start();
			
		}

	}

}
