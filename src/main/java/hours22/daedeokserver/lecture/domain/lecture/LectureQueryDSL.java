package hours22.daedeokserver.lecture.domain.lecture;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hours22.daedeokserver.category.domain.Category;
import hours22.daedeokserver.division.domain.Division;
import hours22.daedeokserver.division.domain.QLectureDivision;
import hours22.daedeokserver.lecture.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LectureQueryDSL {
    @Autowired
    private final JPAQueryFactory jpaQueryFactory;
    private final QLecture lecture = QLecture.lecture;
    private final QLectureDivision division = QLectureDivision.lectureDivision;

    public LectureQueryDSL(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Optional<Lecture> findLecture(Long id) {
        return Optional.ofNullable(jpaQueryFactory.select(lecture)
                .from(lecture)
                .leftJoin(lecture.divisionList, division)
                .fetchJoin()
                .where(lecture.id.eq(id))
                .fetchOne());
    }

    public Page<Lecture> findLecture(Division userDivision, Status status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        page = size * page;

        if (userDivision != null) {
            List<Lecture> lectureList = jpaQueryFactory.select(division.lecture)
                    .from(division)
                    .offset(page)
                    .limit(size)
                    .where((division.lecture.status.eq(status)
                            .and((division.division.eq(userDivision))
                                    .or(division.division.isNull())
                                    .or(division.id.in(
                                            JPAExpressions
                                                    .select(division.id)
                                                    .from(division)
                                                    .where(division.division.firstDivision.eq(userDivision.getFirstDivision())
                                                            .and(division.division.secondDivision.isNull()))
                                    ))
                            )
                    ))
                    .orderBy(division.lecture.id.desc())
                    .fetch();

            long count = jpaQueryFactory.select(division.lecture)
                    .from(division)
                    .where((division.lecture.status.eq(status)
                            .and((division.division.eq(userDivision))
                                    .or(division.division.isNull())
                                    .or(division.id.in(
                                            JPAExpressions
                                                    .select(division.id)
                                                    .from(division)
                                                    .where(division.division.firstDivision.eq(userDivision.getFirstDivision())
                                                            .and(division.division.secondDivision.isNull()))
                                    ))
                            )
                    ))
                    .fetchCount();

            return new PageImpl<>(lectureList, pageRequest, count);
        }

        List<Lecture> lectureList = jpaQueryFactory.select(division.lecture)
                .from(division)
                .offset(page)
                .limit(size)
                .where(division.division.isNull())
                .orderBy(division.lecture.id.desc())
                .fetch();

        long count = jpaQueryFactory.select(division.lecture)
                .from(division)
                .where(division.division.isNull())
                .fetchCount();

        return new PageImpl<>(lectureList, pageRequest, count);
    }

    public Page<Lecture> findLecture(Division userDivision, Status status, String title, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        page = size * page;

        if (userDivision != null) {
            List<Lecture> lectureList = jpaQueryFactory.select(division.lecture)
                    .from(division)
                    .offset(page)
                    .limit(size)
                    .where((division.lecture.status.eq(status)
                            .and((division.division.eq(userDivision))
                                    .or(division.division.isNull())
                                    .or(division.id.in(
                                            JPAExpressions
                                                    .select(division.id)
                                                    .from(division)
                                                    .where(division.division.firstDivision.eq(userDivision.getFirstDivision())
                                                            .and(division.division.secondDivision.isNull()))
                                    ))
                            )
                    )
                            .and(division.lecture.title.contains(title)))
                    .orderBy(division.lecture.id.desc())
                    .fetch();

            long count = jpaQueryFactory.select(division.lecture)
                    .from(division)
                    .where((division.lecture.status.eq(status)
                            .and((division.division.eq(userDivision))
                                    .or(division.division.isNull())
                                    .or(division.id.in(
                                            JPAExpressions
                                                    .select(division.id)
                                                    .from(division)
                                                    .where(division.division.firstDivision.eq(userDivision.getFirstDivision())
                                                            .and(division.division.secondDivision.isNull()))
                                    ))
                            )
                    )
                            .and(division.lecture.title.contains(title)))
                    .fetchCount();

            return new PageImpl<>(lectureList, pageRequest, count);
        }

        List<Lecture> lectureList = jpaQueryFactory.select(division.lecture)
                .from(division)
                .offset(page)
                .limit(size)
                .where(division.division.isNull().and(division.lecture.title.contains(title)))
                .orderBy(division.lecture.id.desc())
                .fetch();

        long count = jpaQueryFactory.select(division.lecture)
                .from(division)
                .where(division.division.isNull().and(division.lecture.title.contains(title)))
                .fetchCount();

        return new PageImpl<>(lectureList, pageRequest, count);
    }

    public Page<Lecture> findLecture(Division userDivision, Status status, Category category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        page = size * page;

        if (userDivision != null) {
            List<Lecture> lectureList = jpaQueryFactory.select(division.lecture)
                    .from(division)
                    .offset(page)
                    .limit(size)
                    .where((division.lecture.status.eq(status)
                            .and((division.division.eq(userDivision))
                                    .or(division.division.isNull())
                                    .or(division.id.in(
                                            JPAExpressions
                                                    .select(division.id)
                                                    .from(division)
                                                    .where(division.division.firstDivision.eq(userDivision.getFirstDivision())
                                                            .and(division.division.secondDivision.isNull()))
                                    ))
                            )
                    )
                            .and(division.lecture.category.eq(category)))
                    .orderBy(division.lecture.id.desc())
                    .fetch();

            long count = jpaQueryFactory.select(division.lecture)
                    .from(division)
                    .where((division.lecture.status.eq(status)
                            .and((division.division.eq(userDivision))
                                    .or(division.division.isNull())
                                    .or(division.id.in(
                                            JPAExpressions
                                                    .select(division.id)
                                                    .from(division)
                                                    .where(division.division.firstDivision.eq(userDivision.getFirstDivision())
                                                            .and(division.division.secondDivision.isNull()))
                                    ))
                            )
                    )
                            .and(division.lecture.category.eq(category)))
                    .fetchCount();

            return new PageImpl<>(lectureList, pageRequest, count);
        }

        List<Lecture> lectureList = jpaQueryFactory.select(division.lecture)
                .from(division)
                .offset(page)
                .limit(size)
                .where(division.division.isNull().and(division.lecture.category.eq(category)))
                .orderBy(division.lecture.id.desc())
                .fetch();

        long count = jpaQueryFactory.select(division.lecture)
                .from(division)
                .where(division.division.isNull().and(division.lecture.category.eq(category)))
                .fetchCount();

        return new PageImpl<>(lectureList, pageRequest, count);
    }

    public Page<Lecture> findLecture(Division userDivision, Status status, Category category, String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        page = size * page;

        if (userDivision != null) {
            List<Lecture> lectureList = jpaQueryFactory.select(division.lecture)
                    .from(division)
                    .offset(page)
                    .limit(size)
                    .where((division.lecture.status.eq(status)
                            .and((division.division.eq(userDivision))
                                    .or(division.division.isNull())
                                    .or(division.id.in(
                                            JPAExpressions
                                                    .select(division.id)
                                                    .from(division)
                                                    .where(division.division.firstDivision.eq(userDivision.getFirstDivision())
                                                            .and(division.division.secondDivision.isNull()))
                                    ))
                            )
                    )
                            .and(division.lecture.category.eq(category))
                            .and(division.lecture.title.contains(keyword)))
                    .orderBy(division.lecture.id.desc())
                    .fetch();

            long count = jpaQueryFactory.select(division.lecture)
                    .from(division)
                    .where((division.lecture.status.eq(status)
                            .and((division.division.eq(userDivision))
                                    .or(division.division.isNull())
                                    .or(division.id.in(
                                            JPAExpressions
                                                    .select(division.id)
                                                    .from(division)
                                                    .where(division.division.firstDivision.eq(userDivision.getFirstDivision())
                                                            .and(division.division.secondDivision.isNull()))
                                    ))
                            )
                    )
                            .and(division.lecture.category.eq(category))
                            .and(division.lecture.title.contains(keyword)))
                    .fetchCount();

            return new PageImpl<>(lectureList, pageRequest, count);
        }

        List<Lecture> lectureList = jpaQueryFactory.select(division.lecture)
                .from(division)
                .offset(page)
                .limit(size)
                .where(division.division.isNull()
                        .and(division.lecture.category.eq(category))
                        .and(division.lecture.title.contains(keyword)))
                .orderBy(division.lecture.id.desc())
                .fetch();

        long count = jpaQueryFactory.select(division.lecture)
                .from(division)
                .where(division.division.isNull()
                        .and(division.lecture.category.eq(category))
                        .and(division.lecture.title.contains(keyword)))
                .fetchCount();

        return new PageImpl<>(lectureList, pageRequest, count);
    }
}
