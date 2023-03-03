package backend.team.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

	@Test
	public void givenUsersInDatabase_whenUserInfoIsRetrived_thenInfoIsRecived()
	throws ClientProtocolException, IOException{
		//Given
		HttpUriRequest request=new HttpGet("http://localhost:8080/usuario");

		//When
		HttpResponse response=HttpClientBuilder.create().build().execute(request);

		//Then
		assertEquals(
			response.getStatusLine().getStatusCode(),HttpStatus.SC_OK
		);
	}

	@Test
	public void givenSpecificUserInDatabase_whenUserInfoIsRetrived_thenInfoIsRecived()
	throws ClientProtocolException, IOException{
		//Given
		int id=(int)(Math.random()*6);
		System.out.println("Probando con el usuario identificado con el id:"+id);
		HttpUriRequest request=new HttpGet("http://localhost:8080/usuario/"+id);

		//When
		HttpResponse response=HttpClientBuilder.create().build().execute(request);

		//Then
		assertEquals(
			response.getStatusLine().getStatusCode(),HttpStatus.SC_OK
		);
	}


}
