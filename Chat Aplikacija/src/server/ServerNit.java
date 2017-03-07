package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Date;

public class ServerNit extends Thread{

	
	boolean zensko;
	LinkedList<ServerNit> klijenti;
	Socket soketZaKomunikaciju = null;
	String ime;
	BufferedReader ulazniTokOdKLijenta = null;
	PrintStream izlazniTokKaKlijentu = null;
	//int port;
	//InetAddress IPAdresa;

	public ServerNit
	(Socket soketZaKomunikaciju, LinkedList<ServerNit> klijenti) {

		this.klijenti = klijenti;
		this.soketZaKomunikaciju = soketZaKomunikaciju;
		//this.IPAdresa = IPAdresa1;
		//this.port = port1;

	}
	
	@Override
	public void run() {

		try {
			
			boolean zensko;
			int i = 0;
			PrintWriter printer = null;
			
			byte[] podaciOdKlijenta = new byte[1024];
			byte[] podaciZaKlijenta = new byte[1024];
			int port;
			InetAddress IPAdresa;
			DatagramPacket paketOdKlijenta = new DatagramPacket(podaciOdKlijenta, podaciOdKlijenta.length);

			
			ulazniTokOdKLijenta = new BufferedReader
					(new InputStreamReader
							(soketZaKomunikaciju.getInputStream()));

			izlazniTokKaKlijentu = new PrintStream
					(soketZaKomunikaciju.getOutputStream());








			//unosenje IMENA
				izlazniTokKaKlijentu.println("Unesite vase ime: ");
				this.ime = ulazniTokOdKLijenta.readLine();
			//zavrsetak unosenja IMENA



			
			
			
			


			//unosenje POLA
			while (true) {
				izlazniTokKaKlijentu.println("Unesite pol('m' ili 'M' za muski, 'z' ili 'Z' za zenski): ");

				String pol1 = ulazniTokOdKLijenta.readLine();

				if(pol1.startsWith("z") || pol1.startsWith("Z")){
					this.zensko = true;
					izlazniTokKaKlijentu.println("Vi ste zena.");
					break;
				}	else if(pol1.startsWith("m") || pol1.startsWith("M")){
					this.zensko = false;
					izlazniTokKaKlijentu.println("Vi ste muskarac.");
					break;
				} else izlazniTokKaKlijentu.println("Pogresno ste uneli, molimo pokusajte ponovo.");
			}
			//zavrsetak unosenja POLA



			
			//obavestenje svima
			while(i < klijenti.size()){
				
				if(klijenti.get(i).zensko != this.zensko && klijenti.get(i) != null 
						&& klijenti.get(i) != this && klijenti.get(i).ime != null){
					
					klijenti.get(i).izlazniTokKaKlijentu.println(this.ime + " se pridruzio/la chat-u.");
					
					printer = new PrintWriter(new BufferedWriter(new FileWriter("Fajl.txt")));
					printer.println(this.ime + " se pridruzio/la chat-u." + "|poslato: " + klijenti.get(i).ime + "|");
					printer.close();
								}				
				i++;
			} 
			//zavrseno obavestenje
			
			
			i=0;






			//unos recenice za slanje
			while(true){	
				
				izlazniTokKaKlijentu.println("Unesite vasu recenicu( 'q' ili 'Q' za kraj): ");
				String recenica = ulazniTokOdKLijenta.readLine();
				
				
				if(recenica.startsWith("q") || recenica.startsWith("Q")){
					izlazniTokKaKlijentu.println("||| Prijatno!");
					
					i=0;
					while(i < klijenti.size()){

						if(klijenti.get(i).zensko != this.zensko && klijenti.get(i) != null 
								&& klijenti.get(i) != this && klijenti.get(i).ime != null){
							
							klijenti.get(i).izlazniTokKaKlijentu.println(this.ime + " je napustio/la chat.");
							
							printer = new PrintWriter(new BufferedWriter(new FileWriter("Fajl.txt")));
							printer.println(this.ime + " je napustio/la chat." + "|poslato: " + klijenti.get(i).ime + "|");
							printer.close();
						}	
						i++;
					} 
					
					klijenti.remove(this);
					break;	
				}
				

				
				
				String primalac = "";

				while (!primalac.contains("k%")) {
				
					//LISTA KLIJENATA
					i = 0;
					izlazniTokKaKlijentu.println("Dobicete listu ispod ___ ('k%' za kraj)");
					
					//dobijanje porta i IP adrese klijenta, kako bi mogli da saljemo listu preko UDP-a
					DatagramSocket datagramSoket = new DatagramSocket(10000);
					datagramSoket.receive(paketOdKlijenta);
					IPAdresa = paketOdKlijenta.getAddress();
					port = paketOdKlijenta.getPort();
					int imaLi = 0;
					while (i < klijenti.size()) {

						if (klijenti.get(i).zensko != this.zensko && klijenti.get(i) != null
								&& klijenti.get(i) != this) {

							podaciZaKlijenta = klijenti.get(i).ime.getBytes();
							DatagramPacket paketZaKlijenta = new DatagramPacket(podaciZaKlijenta,
									podaciZaKlijenta.length, IPAdresa, port);
							datagramSoket.send(paketZaKlijenta);
							imaLi++;

						}

						i++;
					}
					
					String krajcina = "Ovo je kraj liste korisnika.";
					podaciZaKlijenta = krajcina.getBytes();
					DatagramPacket paketZaKlijenta = new DatagramPacket(podaciZaKlijenta, podaciZaKlijenta.length,
							IPAdresa, port);
					datagramSoket.send(paketZaKlijenta);
					datagramSoket.close();
					if (imaLi != 0) {
						
						izlazniTokKaKlijentu.println("Unesite ime jednog primaoca (ukucati 'k%' za kraj.): ");
						primalac = ulazniTokOdKLijenta.readLine();
						int imaLiIkoga = 0;
						i = 0;
						Date datum = new Date();
						while (i < klijenti.size()) {

							if (klijenti.get(i) != null && klijenti.get(i) != this
									&& klijenti.get(i).ime.equals(primalac)
									&& klijenti.get(i).zensko != this.zensko) {

								klijenti.get(i).izlazniTokKaKlijentu.println("<" + this.ime + "> " + recenica);

								printer = new PrintWriter(new BufferedWriter(new FileWriter("Info.txt")));

								printer.println("<" + this.ime+"> je poslao -> "+ klijenti.get(i).ime +" ,recenicu: "+  recenica + " / " + datum);
								printer.close();

								imaLiIkoga = 1;
								break;

							}
							i++;
						}
						if (imaLiIkoga == 0 && !primalac.contains("k%")) {
							izlazniTokKaKlijentu.println("Nema nikoga sa tim imenom, molimo pokusajte ponovo.");
						}


					} 
					else{
						izlazniTokKaKlijentu.println("U listi nema nikoga, molimo da pokusate ponovo.");
						break;
					}
				}




			}




		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
