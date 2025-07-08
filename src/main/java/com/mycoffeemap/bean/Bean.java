package com.mycoffeemap.bean;


import java.util.ArrayList;
import java.util.List;

import com.mycoffeemap.cafe.CafeBean;

import com.mycoffeemap.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "beans")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bean {

	// id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 원두 이름
	@Column(nullable = false)
	private String name;

	// 원두 산지
	private String origin;

	// 로스팅 레벤
	@Enumerated(EnumType.STRING)
	private RoastLevel roastLevel;

	public enum RoastLevel {
		LIGHT, MEDIUM, DARK
	}

	// 향미(플레이버)
	private String flavorNotes;

	// 설명
	@Column(length = 1000)
	private String description;

	// 이미지
	private String imageUrl;

	// 카페빈과 연결
	@OneToMany(mappedBy = "bean", cascade = CascadeType.ALL)
	private List<CafeBean> cafeBeans = new ArrayList<>();

	// 두 개의 Bean 객체가 같은지 비교
	// id 값이 같으면 같은 객체
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Bean))
			return false;
		Bean bean = (Bean) o;
		return id != null && id.equals(bean.id);
	}

	// 객체를 해시 기반 컬렉션(Set, Map 등)에 넣을 때 사용되는 고유 번호
	// id를 기준으로 해시코드를 생성
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	// 연관된 사용자 정보
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	private User user;
	    
}
