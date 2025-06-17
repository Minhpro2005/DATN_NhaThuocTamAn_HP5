<template>
  <div>
    <Header />
    <Navbar />
  </div>

  <div class="container py-4" v-if="tin">
    <h4 class="text-success mb-3">📰 {{ tin.tieuDe }}</h4>

    <p class="text-muted">🗓 Ngày đăng: {{ formatDate(tin.ngayDang) }}</p>

    <img
      v-if="tin.hinhAnh"
      :src="tin.hinhAnh"
      alt="Ảnh bài viết"
      class="img-fluid rounded mb-4"
      style="max-height: 400px; object-fit: cover; width: 100%"
    />

    <!-- Nội dung -->
    <div class="noi-dung mt-4">
      <div v-html="isExpanded ? tin.noiDung : shortContent"></div>
      <div class="text-center mt-3">
        <button class="btn btn-lg btn-success fw-bold px-4" @click="isExpanded = !isExpanded">
          {{ isExpanded ? '▲ Thu gọn' : '▼ Xem thêm' }}
        </button>
      </div>
    </div>
  </div>

  <div v-else class="container py-5 text-center text-muted">
    <p>Không tìm thấy bài viết.</p>
  </div>

  <Footer />
</template>

<script setup>
import Header from '../User/Header.vue'
import Navbar from '../User/Navbar.vue'
import Footer from '../User/Footer.vue'

import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const tin = ref(null)
const isExpanded = ref(false)

const danhSachTinTuc = [
  {
    maTin: '1',
    tieuDe: '5 Cách tăng sức đề kháng hiệu quả',
    ngayDang: '2025-05-28',
    hinhAnh:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/600x400/https://cms-prod.s3-sgn09.fptcloud.com/cum_9666d8fbb3.png',
    noiDung: `<p>Triệu chứng cúm
Những dấu hiệu và triệu chứng của bệnh cúm
Thời kỳ ủ bệnh đối với cúm từ 1 đến 4 ngày với thời gian trung bình khoảng 48 giờ. Trong những trường hợp nhẹ, nhiều triệu chứng giống như cảm lạnh thông thường (ví dụ: Đau họng, chảy nước mũi); viêm kết mạc nhẹ cũng có thể xảy ra.

Các dấu hiệu và triệu chứng của bệnh cúm thường đến đột ngột. Những người bị bệnh cúm thường có các triệu chứng sau:

Sốt (thường trên 38 độ), ớn lạnh, mệt mỏi, đau đầu, đau nhức cơ toàn thân (đặc biệt là ở vùng lưng và chân) và có một trong số các biểu hiện về hô hấp như đau họng, hắt hơi, ngạt mũi, chảy nước mũi, ho, khó thở.

Các triệu chứng hô hấp ban đầu có thể nhẹ, với họng khô và đau, nóng rát dưới xương ức, ho khan và đôi khi là sổ mũi. Bệnh có thể tiến triển thành ho kéo dài và có đàm. 

Một số người có thể bị nôn mửa và tiêu chảy, đặc điểm này phổ biến ở trẻ em hơn người lớn.

Sau 2 đến 3 ngày, các triệu chứng cấp tính sẽ giảm nhanh, mặc dù sốt có thể kéo dài đến 5 ngày. Ho, yếu, đổ mồ hôi và mệt mỏi có thể kéo dài trong vài ngày hoặc đôi khi kéo dài trong nhiều tuần.

Các triệu chứng đường tiêu hóa có thể xảy ra và dường như phổ biến hơn với chủng H1N1. Trẻ em có thể bị buồn nôn, nôn hoặc đau bụng và trẻ sơ sinh có thể biểu hiện hội chứng giống nhiễm trùng huyết.

Sau 2 đến 3 ngày, các triệu chứng cấp tính sẽ nhanh chóng thuyên giảm, mặc dù sốt có thể kéo dài tới 5 ngày. Ho và mệt mỏi có thể kéo dài trong vài ngày hoặc thỉnh thoảng trong nhiều tuần.

Biến chứng có thể gặp khi mắc bệnh cúm 
Một số biến chứng có thể gặp phải khi mắc bệnh cúm có thể kể đến như:

Viêm phổi;
Viêm não;
Viêm cơ tim;
Myoglobin niệu;
Suy thận;
Hội chứng Reye với đặc trưng là bệnh não cấp tính kèm gan nhiễm mỡ thường gặp ở những trẻ đã uống aspirin.
Khi nào cần gặp bác sĩ?
Nếu có bất kỳ triệu chứng nào nêu trên xảy ra, bạn nên liên hệ ngay với bác sĩ để được kiểm tra và tư vấn. Chẩn đoán và điều trị sớm sẽ giảm nguy cơ tăng nặng của bệnh và giúp bạn mau chóng hồi phục sức khỏe.

Nguyên nhân cúm
Cúm mùa là một bệnh nhiễm trùng đường hô hấp cấp tính do virus cúm gây nên. Bệnh xảy ra quanh năm, thường vào mùa đông xuân. Các virus cúm A và B gây ra hầu hết các bệnh ở người và là nguyên nhân gây ra các mùa cúm hàng năm.

Bệnh cúm lây nhiễm thông qua các giọt bắn nhỏ khi nói chuyện, khi ho, hắt hơi. Tại Việt Nam các virus gây bệnh cúm mùa thường gặp là cúm A/H3N2, A/H1N1 và cúm B.

Xem thêm thông tin: Các loại virus cúm phổ biến và hệ luỵ của chúng

Nguy cơ cúm
Những ai có nguy cơ mắc phải (bị) bệnh cúm?
Tất cả mọi người đều có nguy cơ bị bệnh cúm, tuy nhiên một số bệnh nhân có nguy cơ cao bị các biến chứng do cúm, bao gồm:

Trẻ em < 5 tuổi, trẻ suy dinh dưỡng, béo phì, hen phế quản hoặc bị suy giảm miễn dịch bẩm sinh hay mắc phải.
Người lớn > 65 tuổi.
Những người có tình trạng bệnh lý mạn tính (ví dụ: Bệnh tim, bệnh phổi, tiểu đường, suy thận suy giảm miễn dịch,…).
Phụ nữ trong ba tháng thứ 2 hoặc thứ 3 của thai kỳ.
Bệnh nhân có tình trạng bệnh lý làm suy giảm việc bài tiết chất tiết ở đường hô hấp (ví dụ: Suy giảm khả năng nhận thức, rối loạn thần kinh cơ, đột quỵ, các tình trạng động kinh).
Bệnh nhân ≤ 18 tuổi đang dùng aspirin (hội chứng Reye).
Yếu tố làm tăng nguy cơ mắc bệnh cúm
Một số yếu tố làm tăng nguy cơ mắc bệnh cúm, bao gồm:

Người mang thai và những người trong 2 tuần khi kết thúc thai kỳ.
Những người sống trong viện dưỡng lão và các cơ sở chăm sóc dài hạn khác.
Mặc dù tất cả trẻ em dưới 5 tuổi được coi là có nguy cơ mắc các biến chứng cúm nghiêm trọng cao hơn, nhưng nguy cơ cao nhất là ở những trẻ dưới 2 tuổi, với tỷ lệ nhập viện và tử vong cao nhất ở trẻ dưới 6 tháng tuổi.
Phương pháp chẩn đoán & điều trị cúm
Phương pháp xét nghiệm và chẩn đoán bệnh cúm
Chẩn đoán bệnh cúm dựa trên:

Có yếu tố dịch tễ: Sống hoặc đến từ khu vực có cúm lưu hành hoặc tiếp xúc trực tiếp với người bị cúm.
Lâm sàng có sốt (thường trên 38oC), đau nhức cơ toàn thân và có một trong số các biểu hiện về hô hấp như đau họng, hắt hơi, ngạt mũi, chảy nước mũi, ho, khó thở.
Hình ảnh chụp X-quang phổi bình thường hoặc có tổn thương thâm nhiễm lan tỏa ở phổi.
Xét nghiệm công thức máu bạch cầu bình thường hoặc giảm.
Xét nghiệm dương tính với vi rút cúm bằng kỹ thuật RT-PCR hoặc real time RT-PCR hoặc nuôi cấy vi rút đối với các bệnh phẩm là dịch ngoáy họng, dịch tỵ hầu, dịch phế quản.
Phương pháp điều trị bệnh cúm hiệu quả
Điều trị triệu chứng là chủ yếu.

Điều trị cho hầu hết bệnh nhân bị cúm là điều trị triệu chứng, bao gồm nghỉ ngơi, bù nước và hạ sốt khi cần, tránh dùng aspirin cho bệnh nhân ≤ 18 tuổi. Hầu hết bệnh nhân hồi phục hoàn toàn, mặc dù phục hồi hoàn toàn thường mất từ 1 đến 2 tuần. Các trường hợp nhiễm khuẩn phức tạp đòi hỏi phải có kháng sinh thích hợp. 

Tuy nhiên, các trường hợp cúm có biến chứng hoặc có các yếu tố nguy cơ, cần được nhập viện để theo dõi, điều trị và dùng thuốc kháng vi rút càng sớm càng tốt. 

Thuốc điều trị cúm:

Oseltamivir được sử dụng hiện nay là Oseltamivir (Tamiflu) hoặc Zanamivir. Liều lượng Oseltamivir được tính theo lứa tuổi và cân nặng. Thời gian điều trị là 5 ngày. Oseltamivir đôi khi có thể gây buồn nôn và nôn.

Zanamivir dùng theo dạng hít định liều, sử dụng trong các trường hợp không có Oseltamivir hoặc kháng với Oseltamivir. 

Điều trị cúm biến chứng

Hỗ trợ hô hấp khi có suy hô hấp: thở oxy, thở CPAP hoặc thông khí nhân tạo tùy từng trường hợp.
Phát hiện và điều trị sớm các trường hợp bội nhiễm vi khuẩn với kháng sinh thích hợp
Phát hiện và điều trị suy đa phủ tạng.
Điều trị hỗ trợ

Hạ sốt: Chỉ dùng Paracetamol khi sốt trên 38.5 độ, không dùng thuốc nhóm salicylate như aspirin để hạ sốt.
Đảm bảo cân bằng nước điện giải.
Đảm bảo chế độ dinh dưỡng hợp lý.
Xem thêm thông tin: Cách phòng bệnh cúm

Chế độ sinh hoạt & phòng ngừa cúm
Những thói quen sinh hoạt có thể giúp bạn hạn chế diễn tiến của bệnh cúm
Chế độ sinh hoạt:

Tuân thủ theo hướng dẫn của bác sĩ trong việc điều trị. 

Liên hệ ngay với bác sĩ khi cơ thể có những bất thường trong quá trình điều trị.

Thăm khám định kỳ để được theo dõi tình trạng sức khỏe, diễn tiến của bệnh và để bác sĩ tìm hướng điều trị phù hợp trong thời gian tiếp theo nếu bệnh chưa có dấu hiệu thuyên giảm.

Bệnh nhân cần lạc quan. Tâm lý có ảnh hưởng rất lớn đến điều trị, hãy nói chuyện với những người đáng tin cậy, chia sẻ với những thành viên trong gia đình, nuôi thú cưng hay đơn giản là đọc sách, làm bất cứ thứ gì khiến bạn thấy thoải mái. 

Chế độ dinh dưỡng:

Uống nhiều nước tránh mất nước.
Có thể dùng nước chanh mật ong để giảm đau họng.
Ăn các loại trái cây giàu vitamin và khoáng chất, tăng cường hệ miễn dịch như cam, dừa,…
Gừng, tỏi giúp giảm triệu chứng đau đầu, ớn lạnh,…
Ăn sữa chua bổ sung lợi khuẩn cho đường tiêu hóa.
Phương pháp phòng ngừa bệnh cúm hiệu quả
Vắc xin cúm là biện pháp phòng ngừa hiệu quả để giảm nguy cơ mắc bệnh cúm và các biến chứng nghiêm trọng từ căn bệnh. Tiêm vắc xin cúm không chỉ bảo vệ bản thân mà còn giúp bảo vệ cộng đồng khỏi sự lây lan của virus

Tiêm ngừa vắc xin cúm hằng năm với người trên 6 tháng tuổi, đặc biệt đối với những đối tượng mắc cúm có nguy cơ nhập viện cao (bệnh mạn tính, suy giảm miễn dịch, người trên 65 tuổi, trẻ dưới 5 tuổi, phụ nữ mang thai) và những người hành nghề chăm sóc sức khỏe. Theo Tổ chức Y tế Thế giới (WHO), vắc xin cúm đã được chứng minh có hiệu quả trong việc giảm nguy cơ mắc bệnh và tử vong do cúm. Tuy nhiên, do hiệu lực bảo vệ của vắc xin chỉ kéo dài khoảng 1 năm và virus cúm thường xuyên biến đổi cấu trúc kháng nguyên theo chu kỳ hàng năm, việc tiêm nhắc lại vắc xin cúm hàng năm là cần thiết cho cả trẻ em và người lớn.

Hiện nay, Trung tâm tiêm chủng Long Châu có các vắc xin phòng cúm cho trẻ em từ 6 tháng tuổi và người lớn như vắc xin Ivacflu-S (Việt Nam) phòng được 3 chủng cúm, gồm 2 chủng cúm A (H1N1, H3N2), 1 chủng cúm nhóm B (Yamagata hoặc Victoria). Đặc biệt, vắc xin cúm tứ giá Influvac Tetra (Hà Lan), Vaxigrip Tetra (Pháp) phòng được 4 chủng cúm nguy hiểm nhất hiện nay là chủng 2 cúm A (H1N1, H3N2) và 2 chủng cúm B (Yamagata, Victoria).

Vắc xin ngừa cúm đóng vai trò quan trọng trong việc bảo vệ sức khỏe bản thân và miễn dịch cộng đồng. Hiện nay, Trung tâm Tiêm chủng Long Châu tự hào là đơn vị hàng đầu cung cấp dịch vụ tiêm chủng chất lượng, với vắc xin được nhập khẩu chính hãng từ các nhà sản xuất uy tín trên thế giới. Bạn có thể liên hệ với Tiêm chủng Long Châu qua Hotline 1800 6928 để được tư vấn, hẹn lịch miễn phí.

Các loại thuốc kháng virus dự phòng trước khi phơi nhiễm có thể được cân nhắc sử dụng trong một đợt bùng phát dịch bệnh cho bệnh nhân:

Những người mới chỉ chích vắc xin dưới 2 tuần.
Dành cho những người có chống chỉ định với tiêm vắc xin.
Những người bị suy giảm miễn dịch và có thể không đáp ứng với tiêm vắc xin.
Các loại thuốc kháng virus dự phòng sau phơi nhiễm thường được chỉ định cho những người có khả năng bị phơi nhiễm khi có các cụm ca nhiễm trong môi trường kín (ví dụ: Viện dưỡng lão, các khoa của bệnh viện). Cũng có thể dùng những loại thuốc này cho những người tiếp xúc trong gia đình hoặc những người bị phơi nhiễm khác có nguy cơ cao bị các biến chứng của cúm.</p>`, // Toàn bộ nội dung lớn bạn để ở đây
  },
  {
    maTin: '2',
    tieuDe: 'Cảnh báo bệnh tay chân miệng trở lại',
    moTa: 'Bệnh thường gặp ở trẻ dưới 5 tuổi, cần rửa tay sạch.',
    ngayDang: '2025-05-18',
    hinhAnh:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/600x400/https://cms-prod.s3-sgn09.fptcloud.com/tay_chan_mieng_a162a1e04b.png',
    noiDung: `<p><strong>Bệnh tay chân miệng</strong> thường gặp ở trẻ dưới 5 tuổi...</p>`,
  },
  {
    maTin: '3',
    tieuDe: 'Ebola tái bùng phát: Việt Nam cần làm gì?',
    moTa: 'Virus nguy hiểm, tỷ lệ tử vong cao. Bộ Y Tế khuyến cáo không du lịch.',
    ngayDang: '2025-05-15',
    hinhAnh:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/600x400/https://cms-prod.s3-sgn09.fptcloud.com/ebola_073e9acd59.png',
    noiDung: `<p><strong>Ebola</strong> là virus nguy hiểm...</p>`,
  },
]

onMounted(() => {
  const id = route.params.maTin
  tin.value = danhSachTinTuc.find((t) => t.maTin == id)
})

function formatDate(dateStr) {
  const d = new Date(dateStr)
  return d.toLocaleDateString('vi-VN')
}

const shortContent = computed(() => {
  if (!tin.value || !tin.value.noiDung) return ''
  const content = tin.value.noiDung
  const tempDiv = document.createElement('div')
  tempDiv.innerHTML = content
  return tempDiv.innerHTML.slice(0, 600) + '...'
})
</script>

<style scoped>
.noi-dung {
  line-height: 1.7;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.btn-success {
  font-size: 1.1rem;
  border-radius: 999px;
}
</style>
