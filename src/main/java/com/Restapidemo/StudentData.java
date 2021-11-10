package com.Restapidemo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/students")
public class StudentData extends HttpServlet{
	private static final long serialVersionUID = 1L;
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {
		try {
            Connection con = com.Restapidemo.Database.initializeDatabase();
            Statement st = con.createStatement();
            String query = "select * from student";
            if(request.getParameter("id")!="") {
            	query+=" where id = "+request.getParameter("id");
            }	
            ResultSet rs = st.executeQuery(query);
            if(rs.next()==false) {
            	JSONObject js = new JSONObject();
            	js.put("output","Requested ID is not available in the table");
            	response.getWriter().print(js.toString());
            }else {
            	JSONObject sd = new JSONObject();
            	JSONObject osd = new JSONObject();
	            osd.put("id",rs.getInt("id"));  
	            osd.put("name" , rs.getString("name"));  
	            osd.put("dob", rs.getString("dob"));
	            osd.put("phone",rs.getString("phone"));
	            sd.append("Students",osd.toString());
	            while (rs.next()) 
	            {  
		           osd.put("id" , rs.getInt("id"));  
		           osd.put("name", rs.getString("name"));  
		           osd.put("dob" , rs.getString("dob"));
		           osd.put("phone", rs.getString("phone"));
		           sd.append("Students",osd.toString());
	            }
            st.close();
            con.close();
            PrintWriter out = response.getWriter();
            out.print(sd);
            }
       	}
        catch (Exception e) {
        	JSONObject js = new JSONObject();
            js.put("out", e.getMessage());
            response.getWriter().print(js.toString());
        }
	}
	 protected void doPost(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException
		    {
		        try {
		            Connection con = com.Restapidemo.Database.initializeDatabase();
		            PreparedStatement st = con.prepareStatement("insert into student(name,dob,phone) values(?, ?, ?)");
//		            st.setInt(1, Integer.valueOf(request.getParameter("id")));
		            st.setString(1, request.getParameter("name"));
		            st.setString(2, request.getParameter("dob"));
		            st.setString(3, request.getParameter("phone"));
		            st.executeUpdate();
		            st=con.prepareStatement("select count(*) from student");
		            ResultSet rs = st.executeQuery();
		            rs.next();
		            int id=rs.getInt(1);
		            st.close();
		            con.close();
		            JSONObject js = new JSONObject();
		            js.put("out", "New Student added Successfully");
		            js.put("ID of new student",id );
		            response.getWriter().print(js.toString());
		        }
		        catch (Exception e) {
		        	JSONObject js = new JSONObject();
		            js.put("out", e.getMessage());
		            response.getWriter().print(js.toString());
		        }
		    }
	 protected void doPut(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException
		    {
		        try {
		            Connection con = com.Restapidemo.Database.initializeDatabase();
		            Statement st = con.createStatement();
		            String query = "select * from student where id = "+request.getParameter("id");
		            ResultSet rs = st.executeQuery(query);
		            if(rs.next()==false) {
		            	JSONObject js = new JSONObject();
		            	js.put("output","Given ID is not available");
		            	response.getWriter().print(js.toString());
		            	doPost(request,response);
		            	con.close();
		            	st.close();
		            }else {
		            query = "update student set name='"+request.getParameter("name")
		            				+"',dob='"+request.getParameter("dob")
		            				+"',phone='"+request.getParameter("phone")
		            				+"' where id="+Integer.valueOf(request.getParameter("id"));
		            st.executeUpdate(query);
		            st.close();
		            con.close();
		            JSONObject js = new JSONObject();
		            js.put("out", "Updated Successfully");
		            response.getWriter().print(js.toString());
		        }
		      }
		        catch (Exception e) {
		        	JSONObject js = new JSONObject();
		            js.put("out", e.getMessage());
		            response.getWriter().print(js.toString());
		        }
		        
		    }
	 protected void doDelete(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException
		    {
		        try {
		            Connection con = com.Restapidemo.Database.initializeDatabase();
		            Statement st = con.createStatement();
		            String query = "select * from student where id = "+request.getParameter("id");
		            ResultSet rs = st.executeQuery(query);
		            if(rs.next()==false) {
		            	JSONObject js = new JSONObject();
		            	js.put("output","Given ID is not available");
		            	response.getWriter().print(js.toString());
		            	con.close();
		            	st.close();
		            }else {
		            query = "delete from student where id = "+request.getParameter("id");
		            st.execute(query);
		            st.close();
		            con.close();
		            JSONObject js = new JSONObject();
		            js.put("out", "Deleted Successfully");
		            response.getWriter().print(js.toString());
		        }
		       }
		        catch (Exception e) {
		        	JSONObject js = new JSONObject();
		            js.put("out", e.getMessage());
		            response.getWriter().print(js.toString());
		        }
		 
		    }
}
