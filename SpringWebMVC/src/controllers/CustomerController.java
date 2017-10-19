package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dao.NoticeDao;
import vo.Notice;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	
	private NoticeDao noticeDao;
	@Autowired
	public void setNoticeDao(NoticeDao noticeDao) {
		this.noticeDao = noticeDao;
	}
	//@RequestMapping �� �ּ��̴�.
	//������ ���Ŀ��� �ڵ忡 ���� �ּ�!
	@RequestMapping("notice.htm")
	public String notices(String pg, String f, String q, Model model) throws ClassNotFoundException, SQLException{
		/*String _page = request.getParameter("pg");
		String _field = request.getParameter("f");
		String _query = request.getParameter("q");
		*�������� �ȴ�.
		*/
		
		int page = 1;
		String field = "TITLE";
		String query = "%%";
		
		//���ڷ� ���� ���� �־��ָ� �ȴ�.
		if(pg != null && !pg.equals("")){
			 page = Integer.parseInt(pg);
		}
		
		if(f !=null && !f.equals("")){
			field = f;
		}
		
		if(q !=null && !q.equals("")){
			query = q;
		}
		//NoticeDao dao = new NoticeDao();
		
		List<Notice> list = noticeDao.getNotices(page, field, query);
		
		/*ModelAndView mv = new ModelAndView("notice.jsp");
		mv.addObject("list",list);*/
		
		model.addAttribute("list",list);
		//���� �˾Ƽ� �����ǰ� , ��ܸ� ��ȯ���ָ�ȴ�.
		return "notice.jsp";
	}
	@RequestMapping("noticeDetail.htm")
	public String noticeDetail(String seq,Model model) throws ClassNotFoundException, SQLException{
		Notice notice = noticeDao.getNotice(seq);
		model.addAttribute("notice",notice);
		return "noticeDetail.jsp";
	}
	@RequestMapping(value={"noticeReg.htm"}, method=RequestMethod.GET)
	public String noticeReg(){
		return "noticeReg.jsp";
	}
	@RequestMapping(value={"noticeReg.htm"}, method=RequestMethod.POST)
	public String noticeReg(Notice n,HttpServletRequest request/*String title,String content*/) throws ClassNotFoundException, SQLException, IOException{
		//���ε�� ���� ���� Ȯ��
		List<CommonsMultipartFile> files = n.getFile();
		for(int i=0;i<files.size();i++){
			//������ ���ε� ����� Ȯ��.
			if(!/*n.getFile()*/files.get(i).isEmpty()){
			/*n.setTitle(title);
			n.setContent(content);*/
				//1.������ ��Ʈ������ ��ũ�� ����
					//���ϸ� ���.
					String fname = /*n.getFile()*/files.get(i).getOriginalFilename();
					//��θ� ��´�.
					String path = request.getServletContext().getRealPath("/customer/upload");
					String fpath = path + "//" + fname;
					//��½�Ʈ��(������ ����)
					FileOutputStream fs = new FileOutputStream(fpath);
					fs.write(/*n.getFile()*/files.get(i).getBytes());
					fs.close();
					
					System.out.println(fname);
				//2.���ε��� ���ϸ��� �� �÷��� ����
				n.setFileSrc(fname);
				//�������� ������ ���ε� �ȴٸ�
				/*NoticeFile nf = new NoticeFile();
				nf.setNoticeSeq(n.getSeq());
				nf.setFileSrc(fname);
				noticeFileDao.insert(nf);*/
			}
		}		
		noticeDao.insert(n);
		return "redirect:notice.htm"; 
		// ��Ʈ�ѷ��� �ٸ� ��Ʈ�ѷ��� �̵� �� ���� redirect �ٿ��ش�.
	}
	
	@RequestMapping(value={"noticeEdit.htm"}, method=RequestMethod.GET)
	public String noticeEdit(String seq, Model m) throws ClassNotFoundException, SQLException{
		Notice notice =noticeDao.getNotice(seq);
		m.addAttribute("notice",notice);
		return "noticeEdit.jsp";
	}
	@RequestMapping(value={"noticeEdit.htm"}, method=RequestMethod.POST)
	public String noticeEdit(Notice n) throws ClassNotFoundException, SQLException{
		noticeDao.update(n);
		return "redirect:noticeDetail.htm?seq="+n.getSeq(); 
		// ��Ʈ�ѷ��� �ٸ� ��Ʈ�ѷ��� �̵� �� ���� redirect �ٿ��ش�.
	}
	@RequestMapping(value={"noticeDel.htm"}, method=RequestMethod.GET)
	public String noticeDelete(String seq) throws ClassNotFoundException, SQLException{
		noticeDao.delete(seq);
		return "redirect:notice.htm";
	}
	
	@RequestMapping("download.htm")
	public void download(String p,String f,HttpServletResponse response,HttpServletRequest request) throws IOException{
		
		
		
		String fname = new String(f.getBytes("ISO8859_1"),"UTF-8");
		
		response.setHeader(
				"Content-Disposition",
				"attachment;filename="+
				new String( f.getBytes(),"ISO8859_1") 
			);
		
		System.out.println(f);
		
		String fullPath = request.getServletContext().getRealPath(p + "/" + f) ;
		FileInputStream fin = new FileInputStream(fullPath);
		ServletOutputStream sos = response.getOutputStream();
		
		byte[] buf = new byte[1024];
		int size = 0;
		
		while((size = fin.read(buf, 0, 1024)) != -1){
			sos.write(buf, 0, size);
		}
		
		fin.close();
		sos.close();
		
	}
	
}
