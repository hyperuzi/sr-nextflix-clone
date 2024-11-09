import React, { useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router-dom'
import { useContentStore } from '../store/content';
import axios from 'axios';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import ReactPlayer from 'react-player';
import { ORIGINAL_IMG_BASE_URL, SMALL_IMAGE_BASE_URL } from '../utils/constants';
import WatchPageSkeleton from '../components/skeletons/WatchPageSkeleton';


const WatchPage = () => {
    const { id } = useParams();
    const [loading, setLoading] = useState(true);
    const [trailers, setTrailers] = useState([]);
    const [currentTrailerIdx, setCurrentTrailerIdx] = useState(0);
    const [content, setContent] = useState(null);
    const [similarContent, setSimilarContent] = useState([]);
    const { contentType } = useContentStore();
    const sliderRef = useRef(null);
    const [showArrows, setShowArrows] = useState(false);

    const handleNext = () => {
        if (currentTrailerIdx < trailers.length - 1) setCurrentTrailerIdx(currentTrailerIdx + 1);
    }

    const handlePrevious = () => {
        if (currentTrailerIdx > 0) setCurrentTrailerIdx(currentTrailerIdx - 1);
    }

    useEffect(() => {
        const getTrailers = async () => {
            try {
                setLoading(true);
                const res = await axios.get(`/api/v1/${contentType}/${id}/trailers`);
                setTrailers(res.data.trailers);
                setLoading(false);
            } catch (error) {
                if (error.message.includes('404')) {
                    console.log(`no trailers found for id : ${id}`)
                    setTrailers([])
                }
                setLoading(false);
            }
        }
        getTrailers();
    }, [contentType, id]);

    useEffect(() => {
        const getSimilarContent = async () => {
            try {
                setLoading(true);
                const res = await axios.get(`/api/v1/${contentType}/${id}/similar`);
                setSimilarContent(res.data.content);
                setLoading(false);
            } catch (error) {
                if (error.message.includes('404')) {
                    console.log(`no similar content  found for id : ${id}`)
                    setSimilarContent([])
                }
                setLoading(false);
            }

        }
        getSimilarContent();
    }, [contentType, id]);

    useEffect(() => {
        const getContentDetails = async () => {
            try {
                setLoading(true);
                const res = await axios.get(`/api/v1/${contentType}/${id}/details`);
                setContent(res.data.content);
                setLoading(false);
            } catch (error) {
                if (error.message.includes('404')) {
                    console.log(`no trailers found for id : ${id}`)
                    setContent(null)
                }
            } finally {
                setLoading(false);
            }
        }
        getContentDetails();
    }, [contentType, id]);

    function formatReleaseDate(date) {
        return new Date(date).toLocaleDateString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
        })
    }

    const scrollLeft = () => {
        if (sliderRef.current) {
            sliderRef.current.scrollBy({ left: -sliderRef.current.offsetWidth, behavior: "smooth" });
        }
    }
    const scrollRight = () => {
        if (sliderRef.current) {
            sliderRef.current.scrollBy({ left: sliderRef.current.offsetWidth, behavior: "smooth" });
        }
    }
    const formattedContentType = contentType === "movie" ? "Movies" : "TV Shows";

    if(loading) return (
        <div className='min-h-screen bg-black p-10'>
            <WatchPageSkeleton/>
        </div>
    )

    if(content === null) {
        return (
            <div className='bg-black text-white h-screen'>
                <div className='max-w-6xl mx-auto'>
                    <Navbar/>
                    <div className='text-center mx-auto px-4 py-8 h-full mt-40'>
                        <h2 className='text-2xl font-bold text-balance'> Content not found! 🤥</h2>
                    </div>
                </div>
            </div>
        )
    }
    return (
        <div className='bg-black min-h-screen text-white justify-center items-center'>
            <div className='mx-auto container px-4 h-full'>
                <Navbar />
                {trailers.length > 0 && (
                    <div className='flex justify-between items-center mb-4'>
                        <button className={`bg-gray-500/70 hover:bg-gray-500 text-white py-2 px-4 rounded ${currentTrailerIdx === 0 ? " cursor-not-allowed opacity-50" : ""}`} disabled={currentTrailerIdx === 0} onClick={handlePrevious}>
                            <ChevronLeft size={24} />
                        </button>
                        <button className={`bg-gray-500/70 hover:bg-gray-500 text-white py-2 px-4 rounded ${currentTrailerIdx === trailers.length - 1 ? " cursor-not-allowed opacity-50" : ""}`} disabled={currentTrailerIdx === trailers.length - 1} onClick={handleNext}>
                            <ChevronRight size={24} />
                        </button>
                    </div>
                )}
                <div className='aspect-video p-2 sm:px-10 md:px-32'>
                    {trailers?.length > 0 && (
                        <ReactPlayer controls={true} width={"100%"} height={"80%"} className=''
                            url={`https://www.youtube.com/watch?v=${trailers[currentTrailerIdx].key}`} />
                    )}
                    {trailers?.length === 0 && (
                        <h2 className='text-xl text-center mt-5'>
                            No trailers available for {" "}
                            <span className='font-bold text-red-600'>{content.title || content.name}</span> 😪
                        </h2>
                    )}
                </div>
                {/* { movie discription and banner } */}
                <div className='flex flex-col md:flex-row items-center justify-between gap-20 max-w-6xl mx-auto'>

                    <div className='mb-4 md:mb-0'>
                        <h2 className='text-5xl font-bold text-balance'>{content?.name || content?.title}</h2>
                        <p className='mt-2 text-lg'>
                            {formatReleaseDate(content?.release_date || content?.first_air_date)} {" | "} {(content?.adult) ? (<span className='text-red-600'>18+</span>) : (<span className='text-green-600'>PG-13</span>)} {" "}
                        </p>
                        <p className='mt-4 text-lg'>{content?.overview}</p>
                    </div>
                    <img src={ORIGINAL_IMG_BASE_URL + content?.poster_path} alt="Poster Image" className='max-h-[600px] rounded-md' />
                </div>
                {/* SIMILAR MOVIES || TV SHOW */}

                {similarContent?.length > 0 && (
                    <div className='mt-12 max-w-5xl mx-auto relative'>
                        <h3 className='text-3xl font-bold mb-4'>
                            Similar Movies/TV Shows
                        </h3>
                        <div className='flex overflow-x-scroll scrollbar-hide gap-4 pb-4 group' ref={sliderRef}>
                            {similarContent.map((content) => {
                                if(content?.poster_path === null) return null;
                                return (
                                    <Link key={content?.id} to={`/watch/${content?.id}`} className='w-40 flex-none'>
                                        <img src={SMALL_IMAGE_BASE_URL + content?.poster_path} alt="Poster Path" className='w-full h-auto rounded-md' />
                                        <h4 className='mt-2 text-lg font-semibold'>{content?.title || content.name}</h4>
                                    </Link>
                                )
                            })}
                            <ChevronLeft className='absolute top-1/2 -translate-y-1/2 left-7 
                                       size-8 rounded-full bg-black bg-opacity-50 
                                       hover:bg-opacity-75 text-white z-10' onClick={scrollLeft}/>
                            <ChevronRight className='absolute top-1/2 -translate-y-1/2 right-7  items-center 
                                       justify-center size-8 rounded-full bg-black bg-opacity-50 
                                       hover:bg-opacity-75 text-white z-10' onClick={scrollRight}/>
                        </div>
                    </div>
                )}    
            </div>
        </div>
    )
}

export default WatchPage
