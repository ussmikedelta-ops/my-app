
package com.udbhav.solarinspection;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.graphics.pdf.PdfDocument;
import android.graphics.*;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import android.os.Environment;

public class MainActivity extends AppCompatActivity {

EditText customer,contact,address,phone,discom,kno,load,roofArea,moduleType;
Button photoBtn,pdfBtn;
ArrayList<Uri> photos = new ArrayList<>();
static final int PICK_IMAGE = 1;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);

customer = findViewById(R.id.customer);
contact = findViewById(R.id.contact);
address = findViewById(R.id.address);
phone = findViewById(R.id.phone);
discom = findViewById(R.id.discom);
kno = findViewById(R.id.kno);
load = findViewById(R.id.load);
roofArea = findViewById(R.id.roofArea);
moduleType = findViewById(R.id.moduleType);

photoBtn = findViewById(R.id.addPhoto);
pdfBtn = findViewById(R.id.generatePdf);

photoBtn.setOnClickListener(v->{
if(photos.size()>=10){
Toast.makeText(this,"Maximum 10 photos allowed",Toast.LENGTH_SHORT).show();
return;
}
Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
startActivityForResult(intent,PICK_IMAGE);
});

pdfBtn.setOnClickListener(v->generatePdf());
}

@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){
super.onActivityResult(requestCode,resultCode,data);
if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
photos.add(data.getData());
Toast.makeText(this,"Photo Added ("+photos.size()+"/10)",Toast.LENGTH_SHORT).show();
}
}

private void generatePdf(){
try{

PdfDocument document = new PdfDocument();
PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200,2000,1).create();
PdfDocument.Page page = document.startPage(pageInfo);

Canvas canvas = page.getCanvas();
Paint paint = new Paint();
paint.setTextSize(36);

int y = 120;

canvas.drawText("Udbhav Solar Solutions",400,y,paint);
y+=80;

canvas.drawText("Site Inspection Report",420,y,paint);
y+=120;

canvas.drawText("Customer: "+customer.getText(),50,y,paint); y+=60;
canvas.drawText("Contact: "+contact.getText(),50,y,paint); y+=60;
canvas.drawText("Address: "+address.getText(),50,y,paint); y+=60;
canvas.drawText("Phone: "+phone.getText(),50,y,paint); y+=60;
canvas.drawText("DISCOM: "+discom.getText(),50,y,paint); y+=60;
canvas.drawText("K.No: "+kno.getText(),50,y,paint); y+=60;

canvas.drawText("Sanctioned Load: "+load.getText(),50,y,paint); y+=60;
canvas.drawText("Roof Area: "+roofArea.getText(),50,y,paint); y+=60;
canvas.drawText("Module Type: "+moduleType.getText(),50,y,paint); y+=80;

canvas.drawText("Attached Photos: "+photos.size(),50,y,paint);

document.finishPage(page);

File dir = new File(Environment.getExternalStoragePublicDirectory(
Environment.DIRECTORY_DOCUMENTS),"UdbhavSolarReports");

if(!dir.exists()) dir.mkdirs();

File file = new File(dir,"Solar_Inspection_Report.pdf");

document.writeTo(new FileOutputStream(file));
document.close();

Toast.makeText(this,"PDF Saved in Documents/UdbhavSolarReports",Toast.LENGTH_LONG).show();

}catch(Exception e){
e.printStackTrace();
}
}

}
