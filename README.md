Cách sử dụng dự án local như sau:


LƯU Ý : HÃY XÓA TOKEN Ở LOCALSTORAGE ĐI KHI CLONE PROJECT VỀ BỞI VÌ CÙNG LOCALHOST:3000 MÀ MỖI TOKEN THI KHAC NHAU, NẾU KHÔNG XÓA SẼ GÂY LỖI

Backend

Yêu cầu: Postman,IDE (intel,eclipse...),MySQL,JDK
Trong file application.property hãy đổi url front-end thành http://localhost:3000/authenticate tại biến "outbound.identity.redirect-uri"


Frontend
Chạy lệnh npm install ,chạy lệnh npm start để chạy dư án front-end
Link URL backend đuược để trong file RequestAPI và RequestAPIToken  của thư mục utils bên phía front end,hãy đối đường lại đường dẫn thành http://localhost:8080/

Mô tả cơ bản về dự án:

+) Khi login thành công mà header chưa kịp cập nhập hãy reload lại trang web,bạn có thể đăng nhập với google hoặc đăng ký tài khoản

+) Tìm kiếm nâng cao với 16 field tìm kiếm

+)Khi khởi chạy dự án lần đầu tien thì ứng dụng sẽ tự tạo 1 tài khoản là có tên đăng nhập là admin, mật khẩu cũng là admin.Người
admin có toàn quyền bao gồm việc thêm xó sửa tòa nhà, nhân viên, khách hàng,giao dịch...

+) Khi đăng ký thì mặc định sẽ được gắn role là User và muốn cho thành nhân viên thì phải chờ admin duyệt bằng cách cập nhập tài khoản
trong quan ly nhân viên, nhân viên thì sẽ được giao cho quản lý tòa nhà và khách hàng để liên hệ tư vấn

+)Giao dịch thì sẽ có 2 phương thức thanh toán là tiền mặt hoặc chuyển khoản.Với tiền mặt thì nhân viên sẽ cập nhập trên he thong , chuyển 
khoản được tích hợp VNPay

+) Phần liên hệ chính là phần đăng ký tài khoản của customer mỗi tài khoản sẽ chỉ được đăng ký 1 số điện thoại

Database online: "DATABASE_HOST" :"mysql-2da57429-tranquocdat30122004-1fa2.i.aivencloud.com", "DATABASE_PORT" : "15510", "DATABASE_USERNAME" : "avnadmin", "DATABASE_PASSWORD" : "AVNS_6kwbf0N_I5maEe146UP", "DATABASE_NAME" : "realestate"

